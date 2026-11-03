// ffff0000907ae4a8  size=820  FUN_ffff0000907ae4a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907ae4a8
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4,
               long param_5)

{
  uint uVar1;
  long lVar2;
  long local_38;
  
  local_38 = param_5;
  FUN_ffff0000907b7904();
  DAT_ffff000090837e9c = 0x100;
  DAT_ffff000090837ea0 = 0x100;
  _DAT_ffff00009090c50c = 0;
  _DAT_ffff00009090c510 = 0;
  DAT_ffff000090837ea4 = 0x20;
  _DAT_ffff00009090c514 = 0;
  _DAT_ffff00009090c518 = 0;
  _DAT_ffff00009090c51c = 0;
  FUN_ffff00009077eb88(&DAT_ffff00009090c550,0,0x34);
  FUN_ffff00009077eb88(&DAT_ffff00009090c590,0,0x34);
  _DAT_ffff00009090c508 = _DAT_ffff00009090c508 + 1;
  FUN_ffff00009077eb88(0xffff00009090c5c4,0,0x464);
  _DAT_ffff00009090c7c4 = 0x200;
  _DAT_ffff00009090ca0c = 0x30;
  _DAT_ffff00009090ca14 = 4;
  _DAT_ffff00009090c9d0 = 0;
  _DAT_ffff00009090c9c8 = 0x200;
  FUN_ffff00009077eb88(&DAT_ffff00009090ca28,0,0x17c);
  _DAT_ffff00009090ca28 = 0;
  _DAT_ffff00009090ca60 = 0x30;
  _DAT_ffff00009090ca94 = 0x30;
  _DAT_ffff00009090ca9c = 4;
  _DAT_ffff00009090caa4 = 4;
  _DAT_ffff00009090caf0 = 4;
  FUN_ffff0000907b7930();
  FUN_ffff0000907b79a4();
  lVar2 = FUN_ffff0000907b7a18(0x4000);
  if (lVar2 == 0) {
    uVar1 = 7;
  }
  else {
    uVar1 = FUN_ffff0000907ae374(param_2,param_3,param_4,&local_38);
    if (uVar1 != 0) goto LAB_ffff0000907ae690;
    uVar1 = FUN_ffff0000907b7a9c(param_1,lVar2,local_38,0x200);
    if (uVar1 != 0) goto LAB_ffff0000907ae690;
    uVar1 = FUN_ffff0000907ad2fc(lVar2,0x200,&DAT_ffff00009090c590);
    if (uVar1 != 0) goto LAB_ffff0000907ae690;
    if ((_DAT_ffff00009090c590 == 1) && (_DAT_ffff00009090c598 >> 1 == 0x1000000)) {
      if (_DAT_ffff00009090c594 + _DAT_ffff00009090c59c < 0x4001) {
        uVar1 = FUN_ffff0000907b7a9c(param_1,lVar2,local_38);
        if (uVar1 != 0) goto LAB_ffff0000907ae690;
        local_38 = local_38 + (ulong)_DAT_ffff00009090c594 + (ulong)_DAT_ffff00009090c59c;
        uVar1 = FUN_ffff0000907b7a9c(param_1,lVar2,local_38,0x200);
        if (uVar1 != 0) goto LAB_ffff0000907ae690;
        uVar1 = FUN_ffff0000907ad2fc(lVar2,0x200,&DAT_ffff00009090c590);
        if (uVar1 != 0) goto LAB_ffff0000907ae690;
        if (_DAT_ffff00009090c590 != 1) goto LAB_ffff0000907ae7d4;
        uVar1 = 0x10100d;
        if (_DAT_ffff00009090c598 != 0x2000002) goto LAB_ffff0000907ae690;
        if (_DAT_ffff00009090c594 + _DAT_ffff00009090c59c < 0x4001) {
          uVar1 = FUN_ffff0000907b7a9c(param_1,lVar2,local_38);
          if (uVar1 == 0) {
            uVar1 = FUN_ffff0000907b0960
                              (lVar2,_DAT_ffff00009090c59c + _DAT_ffff00009090c594,0x1f400);
            if (uVar1 == 0) {
              FUN_ffff00009077de54
                        (s___s___s_cert_parsing_pass__ffff0000907f39d9,&DAT_ffff0000907f20e7,param_2
                        );
              FUN_ffff0000907b79dc();
              return 0;
            }
          }
          goto LAB_ffff0000907ae690;
        }
      }
      uVar1 = 0x101019;
      goto LAB_ffff0000907ae690;
    }
LAB_ffff0000907ae7d4:
    uVar1 = 0x100d;
  }
  uVar1 = uVar1 | 0x100000;
LAB_ffff0000907ae690:
  FUN_ffff00009077de54
            (s___s___s_cert_parsing_fail__0x_x__ffff0000908032ca,&DAT_ffff0000907f20e7,param_2,uVar1
            );
  FUN_ffff0000907b79dc();
  FUN_ffff0000907aeff0();
  return uVar1;
}


