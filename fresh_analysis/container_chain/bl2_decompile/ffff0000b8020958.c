
undefined8 * FUN_ffff0000b8020958(long param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = (undefined8 *)0x0;
  if (param_1 != 0) {
    if ((DAT_ffff0000b811b690 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b690,param_1), iVar1 != 0)) {
      if ((DAT_ffff0000b811b6a0 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b6a0,param_1), iVar1 != 0)) {
        if ((DAT_ffff0000b811b6b0 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b6b0,param_1), iVar1 != 0)) {
          if ((DAT_ffff0000b811b6c0 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b6c0,param_1), iVar1 != 0)) {
            if ((DAT_ffff0000b811b6d0 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b6d0,param_1), iVar1 != 0)) {
              if ((DAT_ffff0000b811b6e0 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b6e0,param_1), iVar1 != 0)) {
                if ((DAT_ffff0000b811b6f0 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b6f0,param_1), iVar1 != 0)) {
                  if ((DAT_ffff0000b811b700 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b700,param_1), iVar1 != 0)) {
                    if ((DAT_ffff0000b811b710 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b710,param_1), iVar1 != 0)) {
                      if ((DAT_ffff0000b811b720 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b720,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined8 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff0000b811b720;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff0000b811b710;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff0000b811b700;
                  }
                }
                else {
                  puVar2 = &DAT_ffff0000b811b6f0;
                }
              }
              else {
                puVar2 = &DAT_ffff0000b811b6e0;
              }
            }
            else {
              puVar2 = &DAT_ffff0000b811b6d0;
            }
          }
          else {
            puVar2 = &DAT_ffff0000b811b6c0;
          }
        }
        else {
          puVar2 = &DAT_ffff0000b811b6b0;
        }
      }
      else {
        puVar2 = &DAT_ffff0000b811b6a0;
      }
    }
    else {
      puVar2 = &DAT_ffff0000b811b690;
    }
  }
  return puVar2;
}


