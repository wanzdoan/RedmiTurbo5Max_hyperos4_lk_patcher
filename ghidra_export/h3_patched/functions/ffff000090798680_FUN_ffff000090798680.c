// ffff000090798680  size=308  FUN_ffff000090798680

undefined1 FUN_ffff000090798680(long param_1)

{
  undefined1 uVar1;
  int iVar2;
  long lVar3;
  
  if (param_1 == 0) {
LAB_ffff000090798748:
    uVar1 = 4;
  }
  else {
    iVar2 = FUN_ffff00009077ede4(param_1,PTR_DAT_ffff0000908374b0);
    if (iVar2 == 0) {
      lVar3 = 0;
    }
    else {
      iVar2 = FUN_ffff00009077ede4(param_1,PTR_DAT_ffff0000908374c0);
      if (iVar2 == 0) {
        lVar3 = 1;
      }
      else {
        iVar2 = FUN_ffff00009077ede4(param_1,PTR_s_pi_img_ffff0000908374d0);
        if (iVar2 == 0) {
          lVar3 = 2;
        }
        else {
          iVar2 = FUN_ffff00009077ede4(param_1,PTR_DAT_ffff0000908374e0);
          if (iVar2 == 0) {
            lVar3 = 3;
          }
          else {
            iVar2 = FUN_ffff00009077ede4(param_1,PTR_DAT_ffff0000908374f0);
            if (iVar2 == 0) {
              lVar3 = 4;
            }
            else {
              iVar2 = FUN_ffff00009077ede4(param_1,PTR_DAT_ffff000090837500);
              if (iVar2 == 0) {
                lVar3 = 5;
              }
              else {
                iVar2 = FUN_ffff00009077ede4(param_1,PTR_s_tinysys_sspm_ffff000090837510);
                if (iVar2 == 0) {
                  lVar3 = 6;
                }
                else {
                  iVar2 = FUN_ffff00009077ede4(param_1,PTR_s_tinysys_mcupm_ffff000090837520);
                  if (iVar2 == 0) {
                    lVar3 = 7;
                  }
                  else {
                    iVar2 = FUN_ffff00009077ede4
                                      (param_1,PTR_s_tinysys_mcupm_RV33_A_ffff000090837530);
                    if (iVar2 != 0) goto LAB_ffff000090798748;
                    lVar3 = 8;
                  }
                }
              }
            }
          }
        }
      }
    }
    uVar1 = (&DAT_ffff0000908374b8)[lVar3 * 0x10];
  }
  return uVar1;
}


