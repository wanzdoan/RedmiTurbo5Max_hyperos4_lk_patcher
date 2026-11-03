
undefined8 * FUN_ffff0000be02956c(long param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = (undefined8 *)0x0;
  if (param_1 != 0) {
    if ((DAT_ffff0000be117f58 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f58,param_1), iVar1 != 0)) {
      if ((DAT_ffff0000be117f70 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f70,param_1), iVar1 != 0)) {
        if ((DAT_ffff0000be117f88 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f88,param_1), iVar1 != 0)) {
          if ((DAT_ffff0000be117fa0 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fa0,param_1), iVar1 != 0)) {
            if ((DAT_ffff0000be117fb8 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fb8,param_1), iVar1 != 0)) {
              if ((DAT_ffff0000be117fd0 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fd0,param_1), iVar1 != 0)) {
                if ((DAT_ffff0000be117fe8 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117fe8,param_1), iVar1 != 0)) {
                  if ((DAT_ffff0000be118000 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be118000,param_1), iVar1 != 0)) {
                    if ((DAT_ffff0000be118018 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be118018,param_1), iVar1 != 0)) {
                      if ((DAT_ffff0000be118030 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be118030,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined8 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff0000be118030;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff0000be118018;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff0000be118000;
                  }
                }
                else {
                  puVar2 = &DAT_ffff0000be117fe8;
                }
              }
              else {
                puVar2 = &DAT_ffff0000be117fd0;
              }
            }
            else {
              puVar2 = &DAT_ffff0000be117fb8;
            }
          }
          else {
            puVar2 = &DAT_ffff0000be117fa0;
          }
        }
        else {
          puVar2 = &DAT_ffff0000be117f88;
        }
      }
      else {
        puVar2 = &DAT_ffff0000be117f70;
      }
    }
    else {
      puVar2 = &DAT_ffff0000be117f58;
    }
  }
  return puVar2;
}


