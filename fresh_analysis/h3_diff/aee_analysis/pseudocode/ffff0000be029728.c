
undefined4 FUN_ffff0000be029728(long param_1,undefined8 param_2,undefined8 *param_3)

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
  if (DAT_ffff0000be117f58 == (undefined8 *)0x0) {
    lVar3 = 0;
    puVar2 = &DAT_ffff0000be117f58;
  }
  else {
    iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f58,*param_3);
    if (iVar1 == 0) {
      return 0xfffffff8;
    }
    puVar2 = &DAT_ffff0000be117f70;
    if (DAT_ffff0000be117f70 == (undefined8 *)0x0) {
      lVar3 = 1;
    }
    else {
      iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f70,*param_3);
      if (iVar1 == 0) {
        return 0xfffffff8;
      }
      puVar2 = &DAT_ffff0000be117f88;
      if (DAT_ffff0000be117f88 == (undefined8 *)0x0) {
        lVar3 = 2;
      }
      else {
        iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f88,*param_3);
        if (iVar1 == 0) {
          return 0xfffffff8;
        }
        puVar2 = &DAT_ffff0000be117fa0;
        if (DAT_ffff0000be117fa0 == (undefined8 *)0x0) {
          lVar3 = 3;
        }
        else {
          iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fa0,*param_3);
          if (iVar1 == 0) {
            return 0xfffffff8;
          }
          puVar2 = &DAT_ffff0000be117fb8;
          if (DAT_ffff0000be117fb8 == (undefined8 *)0x0) {
            lVar3 = 4;
          }
          else {
            iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fb8,*param_3);
            if (iVar1 == 0) {
              return 0xfffffff8;
            }
            puVar2 = &DAT_ffff0000be117fd0;
            if (DAT_ffff0000be117fd0 == (undefined8 *)0x0) {
              lVar3 = 5;
            }
            else {
              iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fd0,*param_3);
              if (iVar1 == 0) {
                return 0xfffffff8;
              }
              puVar2 = &DAT_ffff0000be117fe8;
              if (DAT_ffff0000be117fe8 == (undefined8 *)0x0) {
                lVar3 = 6;
              }
              else {
                iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fe8,*param_3);
                if (iVar1 == 0) {
                  return 0xfffffff8;
                }
                puVar2 = &DAT_ffff0000be118000;
                if (DAT_ffff0000be118000 == (undefined8 *)0x0) {
                  lVar3 = 7;
                }
                else {
                  iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be118000,*param_3);
                  if (iVar1 == 0) {
                    return 0xfffffff8;
                  }
                  puVar2 = &DAT_ffff0000be118018;
                  if (DAT_ffff0000be118018 == (undefined8 *)0x0) {
                    lVar3 = 8;
                  }
                  else {
                    iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be118018,*param_3);
                    if (iVar1 == 0) {
                      return 0xfffffff8;
                    }
                    puVar2 = &DAT_ffff0000be118030;
                    if (DAT_ffff0000be118030 != (undefined8 *)0x0) {
                      iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be118030,*param_3);
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
  (&DAT_ffff0000be117f60)[lVar3 * 3] = param_1;
  (&DAT_ffff0000be117f68)[lVar3 * 3] = param_2;
  return 0;
}


