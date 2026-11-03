
undefined8 * FUN_ffff0000b8020e20(long param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = (undefined8 *)0x0;
  if (param_1 != 0) {
    if ((DAT_ffff0000b811b738 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b738,param_1), iVar1 != 0)) {
      if ((DAT_ffff0000b811b750 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b750,param_1), iVar1 != 0)) {
        if ((DAT_ffff0000b811b768 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b768,param_1), iVar1 != 0)) {
          if ((DAT_ffff0000b811b780 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b780,param_1), iVar1 != 0)) {
            if ((DAT_ffff0000b811b798 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b798,param_1), iVar1 != 0)) {
              if ((DAT_ffff0000b811b7b0 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7b0,param_1), iVar1 != 0)) {
                if ((DAT_ffff0000b811b7c8 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7c8,param_1), iVar1 != 0)) {
                  if ((DAT_ffff0000b811b7e0 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7e0,param_1), iVar1 != 0)) {
                    if ((DAT_ffff0000b811b7f8 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b7f8,param_1), iVar1 != 0)) {
                      if ((DAT_ffff0000b811b810 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff0000b804d248(*DAT_ffff0000b811b810,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined8 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff0000b811b810;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff0000b811b7f8;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff0000b811b7e0;
                  }
                }
                else {
                  puVar2 = &DAT_ffff0000b811b7c8;
                }
              }
              else {
                puVar2 = &DAT_ffff0000b811b7b0;
              }
            }
            else {
              puVar2 = &DAT_ffff0000b811b798;
            }
          }
          else {
            puVar2 = &DAT_ffff0000b811b780;
          }
        }
        else {
          puVar2 = &DAT_ffff0000b811b768;
        }
      }
      else {
        puVar2 = &DAT_ffff0000b811b750;
      }
    }
    else {
      puVar2 = &DAT_ffff0000b811b738;
    }
  }
  return puVar2;
}


