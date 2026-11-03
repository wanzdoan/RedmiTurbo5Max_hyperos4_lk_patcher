
undefined8 * FUN_ffff000090736af8(long param_1)

{
  int iVar1;
  undefined8 *puVar2;
  
  puVar2 = (undefined8 *)0x0;
  if (param_1 != 0) {
    if ((DAT_ffff00009086dbf0 == (undefined8 *)0x0) ||
       (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dbf0,param_1), iVar1 != 0)) {
      if ((DAT_ffff00009086dc00 == (undefined8 *)0x0) ||
         (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc00,param_1), iVar1 != 0)) {
        if ((DAT_ffff00009086dc10 == (undefined8 *)0x0) ||
           (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc10,param_1), iVar1 != 0)) {
          if ((DAT_ffff00009086dc20 == (undefined8 *)0x0) ||
             (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc20,param_1), iVar1 != 0)) {
            if ((DAT_ffff00009086dc30 == (undefined8 *)0x0) ||
               (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc30,param_1), iVar1 != 0)) {
              if ((DAT_ffff00009086dc40 == (undefined8 *)0x0) ||
                 (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc40,param_1), iVar1 != 0)) {
                if ((DAT_ffff00009086dc50 == (undefined8 *)0x0) ||
                   (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc50,param_1), iVar1 != 0)) {
                  if ((DAT_ffff00009086dc60 == (undefined8 *)0x0) ||
                     (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc60,param_1), iVar1 != 0)) {
                    if ((DAT_ffff00009086dc70 == (undefined8 *)0x0) ||
                       (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc70,param_1), iVar1 != 0)) {
                      if ((DAT_ffff00009086dc80 == (undefined8 *)0x0) ||
                         (iVar1 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc80,param_1), iVar1 != 0))
                      {
                        puVar2 = (undefined8 *)0x0;
                      }
                      else {
                        puVar2 = &DAT_ffff00009086dc80;
                      }
                    }
                    else {
                      puVar2 = &DAT_ffff00009086dc70;
                    }
                  }
                  else {
                    puVar2 = &DAT_ffff00009086dc60;
                  }
                }
                else {
                  puVar2 = &DAT_ffff00009086dc50;
                }
              }
              else {
                puVar2 = &DAT_ffff00009086dc40;
              }
            }
            else {
              puVar2 = &DAT_ffff00009086dc30;
            }
          }
          else {
            puVar2 = &DAT_ffff00009086dc20;
          }
        }
        else {
          puVar2 = &DAT_ffff00009086dc10;
        }
      }
      else {
        puVar2 = &DAT_ffff00009086dc00;
      }
    }
    else {
      puVar2 = &DAT_ffff00009086dbf0;
    }
  }
  return puVar2;
}


