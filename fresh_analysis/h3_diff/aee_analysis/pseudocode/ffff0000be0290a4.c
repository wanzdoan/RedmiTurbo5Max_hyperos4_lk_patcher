
undefined8 * FUN_ffff0000be0290a4(long param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = (undefined8 *)0x0;
  if (param_1 != 0) {
    if ((DAT_ffff0000be117eb0 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117eb0,param_1), iVar1 != 0)) {
      if ((DAT_ffff0000be117ec0 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117ec0,param_1), iVar1 != 0)) {
        if ((DAT_ffff0000be117ed0 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117ed0,param_1), iVar1 != 0)) {
          if ((DAT_ffff0000be117ee0 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117ee0,param_1), iVar1 != 0)) {
            if ((DAT_ffff0000be117ef0 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117ef0,param_1), iVar1 != 0)) {
              if ((DAT_ffff0000be117f00 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f00,param_1), iVar1 != 0)) {
                if ((DAT_ffff0000be117f10 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f10,param_1), iVar1 != 0)) {
                  if ((DAT_ffff0000be117f20 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f20,param_1), iVar1 != 0)) {
                    if ((DAT_ffff0000be117f30 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f30,param_1), iVar1 != 0)) {
                      if ((DAT_ffff0000be117f40 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff0000be060cb8(*DAT_ffff0000be117f40,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined8 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff0000be117f40;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff0000be117f30;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff0000be117f20;
                  }
                }
                else {
                  puVar2 = &DAT_ffff0000be117f10;
                }
              }
              else {
                puVar2 = &DAT_ffff0000be117f00;
              }
            }
            else {
              puVar2 = &DAT_ffff0000be117ef0;
            }
          }
          else {
            puVar2 = &DAT_ffff0000be117ee0;
          }
        }
        else {
          puVar2 = &DAT_ffff0000be117ed0;
        }
      }
      else {
        puVar2 = &DAT_ffff0000be117ec0;
      }
    }
    else {
      puVar2 = &DAT_ffff0000be117eb0;
    }
  }
  return puVar2;
}


