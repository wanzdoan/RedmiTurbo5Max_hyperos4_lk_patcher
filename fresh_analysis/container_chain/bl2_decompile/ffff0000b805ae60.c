
undefined1 FUN_ffff0000b805ae60(long param_1)

{
  undefined1 uVar1;
  int iVar2;
  long lVar3;
  
  if (param_1 == 0) {
LAB_ffff0000b805af28:
    uVar1 = 4;
  }
  else {
    iVar2 = FUN_ffff0000b804d248(param_1,PTR_DAT_ffff0000b8106d00);
    if (iVar2 == 0) {
      lVar3 = 0;
    }
    else {
      iVar2 = FUN_ffff0000b804d248(param_1,PTR_DAT_ffff0000b8106d10);
      if (iVar2 == 0) {
        lVar3 = 1;
      }
      else {
        iVar2 = FUN_ffff0000b804d248(param_1,PTR_s_pi_img_ffff0000b8106d20);
        if (iVar2 == 0) {
          lVar3 = 2;
        }
        else {
          iVar2 = FUN_ffff0000b804d248(param_1,PTR_DAT_ffff0000b8106d30);
          if (iVar2 == 0) {
            lVar3 = 3;
          }
          else {
            iVar2 = FUN_ffff0000b804d248(param_1,PTR_DAT_ffff0000b8106d40);
            if (iVar2 == 0) {
              lVar3 = 4;
            }
            else {
              iVar2 = FUN_ffff0000b804d248(param_1,PTR_DAT_ffff0000b8106d50);
              if (iVar2 == 0) {
                lVar3 = 5;
              }
              else {
                iVar2 = FUN_ffff0000b804d248(param_1,PTR_s_tinysys_sspm_ffff0000b8106d60);
                if (iVar2 == 0) {
                  lVar3 = 6;
                }
                else {
                  iVar2 = FUN_ffff0000b804d248(param_1,PTR_s_tinysys_mcupm_ffff0000b8106d70);
                  if (iVar2 == 0) {
                    lVar3 = 7;
                  }
                  else {
                    iVar2 = FUN_ffff0000b804d248
                                      (param_1,PTR_s_tinysys_mcupm_RV33_A_ffff0000b8106d80);
                    if (iVar2 != 0) goto LAB_ffff0000b805af28;
                    lVar3 = 8;
                  }
                }
              }
            }
          }
        }
      }
    }
    uVar1 = (&DAT_ffff0000b8106d08)[lVar3 * 0x10];
  }
  return uVar1;
}


