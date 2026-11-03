
undefined4 FUN_ffff0000b8020fdc(long param_1,undefined8 param_2,undefined8 *param_3)

{
  int iVar1;
  undefined8 *puVar2;
  long lVar3;
  
  if (param_1 == 0) {
    return 0xfffffff8;
  }
  if (param_3 == (undefined8 *)0x0) {
    return 0xfffffff8;
  }
  if (DAT_ffff0000b811b738 == (undefined8 *)0x0) {
    lVar3 = 0;
    puVar2 = &DAT_ffff0000b811b738;
  }
  else {
    iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b738,*param_3);
    if (iVar1 == 0) {
      return 0xfffffff8;
    }
    puVar2 = &DAT_ffff0000b811b750;
    if (DAT_ffff0000b811b750 == (undefined8 *)0x0) {
      lVar3 = 1;
    }
    else {
      iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b750,*param_3);
      if (iVar1 == 0) {
        return 0xfffffff8;
      }
      puVar2 = &DAT_ffff0000b811b768;
      if (DAT_ffff0000b811b768 == (undefined8 *)0x0) {
        lVar3 = 2;
      }
      else {
        iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b768,*param_3);
        if (iVar1 == 0) {
          return 0xfffffff8;
        }
        puVar2 = &DAT_ffff0000b811b780;
        if (DAT_ffff0000b811b780 == (undefined8 *)0x0) {
          lVar3 = 3;
        }
        else {
          iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b780,*param_3);
          if (iVar1 == 0) {
            return 0xfffffff8;
          }
          puVar2 = &DAT_ffff0000b811b798;
          if (DAT_ffff0000b811b798 == (undefined8 *)0x0) {
            lVar3 = 4;
          }
          else {
            iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b798,*param_3);
            if (iVar1 == 0) {
              return 0xfffffff8;
            }
            puVar2 = &DAT_ffff0000b811b7b0;
            if (DAT_ffff0000b811b7b0 == (undefined8 *)0x0) {
              lVar3 = 5;
            }
            else {
              iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7b0,*param_3);
              if (iVar1 == 0) {
                return 0xfffffff8;
              }
              puVar2 = &DAT_ffff0000b811b7c8;
              if (DAT_ffff0000b811b7c8 == (undefined8 *)0x0) {
                lVar3 = 6;
              }
              else {
                iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7c8,*param_3);
                if (iVar1 == 0) {
                  return 0xfffffff8;
                }
                puVar2 = &DAT_ffff0000b811b7e0;
                if (DAT_ffff0000b811b7e0 == (undefined8 *)0x0) {
                  lVar3 = 7;
                }
                else {
                  iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7e0,*param_3);
                  if (iVar1 == 0) {
                    return 0xfffffff8;
                  }
                  puVar2 = &DAT_ffff0000b811b7f8;
                  if (DAT_ffff0000b811b7f8 == (undefined8 *)0x0) {
                    lVar3 = 8;
                  }
                  else {
                    iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7f8,*param_3);
                    if (iVar1 == 0) {
                      return 0xfffffff8;
                    }
                    puVar2 = &DAT_ffff0000b811b810;
                    if (DAT_ffff0000b811b810 != (undefined8 *)0x0) {
                      iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b810,*param_3);
                      if (iVar1 != 0) {
                        return 0;
                      }
                      return 0xfffffff8;
                    }
                    lVar3 = 9;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  *puVar2 = param_3;
  (&DAT_ffff0000b811b740)[lVar3 * 3] = param_1;
  (&DAT_ffff0000b811b748)[lVar3 * 3] = param_2;
  return 0;
}


