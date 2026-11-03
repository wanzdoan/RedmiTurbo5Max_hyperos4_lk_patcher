
void FUN_ffff0000b80121fc(void)

{
  uint local_8;
  uint local_4;
  
  local_8 = 0;
  FUN_ffff0000b8012974(&local_8);
  if (DAT_ffff0000b811aeb4 <= (int)(local_8 / 1000)) {
    local_8 = 0;
    FUN_ffff0000b8012974(&local_8);
    if (DAT_ffff0000b811aeb4 <= (int)(local_8 / 1000)) {
      local_8 = 0;
      FUN_ffff0000b8012974(&local_8);
      if (DAT_ffff0000b811aeb4 <= (int)(local_8 / 1000)) {
        if (DAT_ffff0000b811aea0._1_1_ == '\0') {
          return;
        }
        local_4 = 0;
        FUN_ffff0000b8012974(&local_4);
        if ((int)(local_4 / 1000) <= DAT_ffff0000b811aeb0) {
          return;
        }
        FUN_ffff0000b804c33c
                  (s__s__d___XMCHG_LK___s__vbus__d_ov_ffff0000b809d51a,
                   s_check_charger_volt_ffff0000b809d507,0x31d,s_check_charger_volt_ffff0000b809d507
                  );
        FUN_ffff0000b801b994();
        FUN_ffff0000b8051b40();
        FUN_ffff0000b804b704(4000000);
        FUN_ffff0000b801ba64();
        FUN_ffff0000b80520cc(0);
        goto LAB_ffff0000b80122fc;
      }
    }
  }
  FUN_ffff0000b804c33c
            (s__s__d___XMCHG_LK___s__vbus__d_is_ffff0000b8088be9,
             s_check_charger_volt_ffff0000b809d507,0x314,s_check_charger_volt_ffff0000b809d507);
LAB_ffff0000b80122fc:
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b8014dbc(2,3);
}


