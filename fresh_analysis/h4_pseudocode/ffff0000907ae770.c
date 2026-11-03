
uint FUN_ffff0000907ae770
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4,
               long param_5)

{
  uint uVar1;
  long lVar2;
  long local_38;
  
  local_38 = param_5;
  FUN_ffff0000907b7f78();
  DAT_ffff000090837ebc = 0x100;
  DAT_ffff000090837ec0 = 0x100;
  DAT_ffff00009090c5cc = 0;
  DAT_ffff00009090c5d0 = 0;
  DAT_ffff000090837ec4 = 0x20;
  DAT_ffff00009090c5d4 = 0;
  DAT_ffff00009090c5d8 = 0;
  DAT_ffff00009090c5dc = 0;
  FUN_ffff00009077ee50(&DAT_ffff00009090c610,0,0x34);
  FUN_ffff00009077ee50(&DAT_ffff00009090c650,0,0x34);
  DAT_ffff00009090c5c8 = DAT_ffff00009090c5c8 + 1;
  FUN_ffff00009077ee50(&DAT_ffff00009090c684,0,0x464);
  DAT_ffff00009090c884 = 0x200;
  DAT_ffff00009090cacc = 0x30;
  DAT_ffff00009090cad4 = 4;
  DAT_ffff00009090ca90 = 0;
  DAT_ffff00009090ca88 = 0x200;
  FUN_ffff00009077ee50(&DAT_ffff00009090cae8,0,0x17c);
  DAT_ffff00009090cae8 = 0;
  DAT_ffff00009090cb20 = 0x30;
  DAT_ffff00009090cb54 = 0x30;
  DAT_ffff00009090cb5c = 4;
  DAT_ffff00009090cb64 = 4;
  DAT_ffff00009090cbb0 = 4;
  FUN_ffff0000907b7fa4();
  FUN_ffff0000907b8018();
  lVar2 = FUN_ffff0000907b808c(0x4000);
  if (lVar2 == 0) {
    uVar1 = 7;
  }
  else {
    uVar1 = FUN_ffff0000907ae63c(param_2,param_3,param_4,&local_38);
    if (uVar1 != 0) goto LAB_ffff0000907ae958;
    uVar1 = FUN_ffff0000907b8110(param_1,lVar2,local_38,0x200);
    if (uVar1 != 0) goto LAB_ffff0000907ae958;
    uVar1 = FUN_ffff0000907ad5bc(lVar2,0x200,&DAT_ffff00009090c650);
    if (uVar1 != 0) goto LAB_ffff0000907ae958;
    if ((DAT_ffff00009090c650 == 1) && (DAT_ffff00009090c658 >> 1 == 0x1000000)) {
      if (DAT_ffff00009090c654 + DAT_ffff00009090c65c < 0x4001) {
        uVar1 = FUN_ffff0000907b8110(param_1,lVar2,local_38);
        if (uVar1 != 0) goto LAB_ffff0000907ae958;
        local_38 = local_38 + (ulong)DAT_ffff00009090c654 + (ulong)DAT_ffff00009090c65c;
        uVar1 = FUN_ffff0000907b8110(param_1,lVar2,local_38,0x200);
        if (uVar1 != 0) goto LAB_ffff0000907ae958;
        uVar1 = FUN_ffff0000907ad5bc(lVar2,0x200,&DAT_ffff00009090c650);
        if (uVar1 != 0) goto LAB_ffff0000907ae958;
        if (DAT_ffff00009090c650 != 1) goto LAB_ffff0000907aea9c;
        uVar1 = 0x10100d;
        if (DAT_ffff00009090c658 != 0x2000002) goto LAB_ffff0000907ae958;
        if (DAT_ffff00009090c654 + DAT_ffff00009090c65c < 0x4001) {
          uVar1 = FUN_ffff0000907b8110(param_1,lVar2,local_38);
          if (uVar1 == 0) {
            uVar1 = FUN_ffff0000907b0ec4(lVar2,DAT_ffff00009090c65c + DAT_ffff00009090c654,0x1f400);
            if (uVar1 == 0) {
              FUN_ffff00009077e11c
                        (s___s___s_cert_parsing_pass__ffff0000907f3b7c,&DAT_ffff0000907f228a,param_2
                        );
              FUN_ffff0000907b8050();
              return 0;
            }
          }
          goto LAB_ffff0000907ae958;
        }
      }
      uVar1 = 0x101019;
      goto LAB_ffff0000907ae958;
    }
LAB_ffff0000907aea9c:
    uVar1 = 0x100d;
  }
  uVar1 = uVar1 | 0x100000;
LAB_ffff0000907ae958:
  FUN_ffff00009077e11c
            (s___s___s_cert_parsing_fail__0x_x__ffff0000908034f2,&DAT_ffff0000907f228a,param_2,uVar1
            );
  FUN_ffff0000907b8050();
  FUN_ffff0000907af2b8();
  return uVar1;
}


