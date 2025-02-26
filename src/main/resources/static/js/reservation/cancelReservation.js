import { showAlertToast } from "/js/utils/showAlertToast.js";
import { showConfirmToast } from "/js/utils/showConfirmToast.js";
import { fetchReservations } from "/js/reservation/reservationList.js";

export async function cancelReservation(reservationId) {
    showConfirmToast(
        "정말 예약을 취소하시겠습니까?",
        async () => {
            try {
                const response = await fetch(`/api/v1/reservations/${reservationId}`, {
                    method: "DELETE",
                });

                if (!response.ok) {
                    throw new Error(`예약 취소 실패: ${response.status}`);
                }

                await fetchReservations(); // 최신 예약 목록을 다시 불러오기

                showAlertToast("예약이 취소되었습니다.");
            } catch (error) {
                showAlertToast("예약 취소 실패: " + error.message);
            }
        }
    );
}


