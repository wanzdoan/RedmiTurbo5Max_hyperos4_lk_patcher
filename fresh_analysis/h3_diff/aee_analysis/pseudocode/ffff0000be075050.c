
undefined1 FUN_ffff0000be075050(long param_1)

{
  undefined1 uVar1;
  int iVar2;
  long lVar3;
  
  if (param_1 == 0) {
LAB_ffff0000be075118:
    uVar1 = 4;
  }
  else {
    iVar2 = FUN_ffff0000be060cb8(param_1,PTR_DAT_ffff0000be0f36a8);
    if (iVar2 == 0) {
      lVar3 = 0;
    }
    else {
      iVar2 = FUN_ffff0000be060cb8(param_1,PTR_DAT_ffff0000be0f36b8);
      if (iVar2 == 0) {
        lVar3 = 1;
      }
      else {
        iVar2 = FUN_ffff0000be060cb8(param_1,PTR_s_pi_img_ffff0000be0f36c8);
        if (iVar2 == 0) {
          lVar3 = 2;
        }
        else {
          iVar2 = FUN_ffff0000be060cb8(param_1,PTR_DAT_ffff0000be0f36d8);
          if (iVar2 == 0) {
            lVar3 = 3;
          }
          else {
            iVar2 = FUN_ffff0000be060cb8(param_1,PTR_DAT_ffff0000be0f36e8);
            if (iVar2 == 0) {
              lVar3 = 4;
            }
            else {
              iVar2 = FUN_ffff0000be060cb8(param_1,PTR_DAT_ffff0000be0f36f8);
              if (iVar2 == 0) {
                lVar3 = 5;
              }
              else {
                iVar2 = FUN_ffff0000be060cb8(param_1,PTR_s_tinysys_sspm_ffff0000be0f3708);
                if (iVar2 == 0) {
                  lVar3 = 6;
                }
                else {
                  iVar2 = FUN_ffff0000be060cb8(param_1,PTR_s_tinysys_mcupm_ffff0000be0f3718);
                  if (iVar2 == 0) {
                    lVar3 = 7;
                  }
                  else {
                    iVar2 = FUN_ffff0000be060cb8
                                      (param_1,PTR_s_tinysys_mcupm_RV33_A_ffff0000be0f3728);
                    if (iVar2 != 0) goto LAB_ffff0000be075118;
                    lVar3 = 8;
                  }
                }
              }
            }
          }
        }
      }
    }
    uVar1 = (&DAT_ffff0000be0f36b0)[lVar3 * 0x10];
  }
  return uVar1;
}


