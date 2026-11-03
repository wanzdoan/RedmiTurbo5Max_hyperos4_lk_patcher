
uint FUN_ffff0000be088964
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4,
               long param_5)

{
  uint uVar1;
  long lVar2;
  long local_38;
  
  local_38 = param_5;
  FUN_ffff0000be091d9c();
  DAT_ffff0000be0f4094 = 0x100;
  DAT_ffff0000be0f4098 = 0x100;
  DAT_ffff0000be1b6ac4 = 0;
  DAT_ffff0000be1b6ac8 = 0;
  DAT_ffff0000be0f409c = 0x20;
  DAT_ffff0000be1b6acc = 0;
  DAT_ffff0000be1b6ad0 = 0;
  DAT_ffff0000be1b6ad4 = 0;
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b6b08,0,0x34);
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b6b48,0,0x34);
  DAT_ffff0000be1b6ac0 = DAT_ffff0000be1b6ac0 + 1;
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b6b7c,0,0x464);
  DAT_ffff0000be1b6d7c = 0x200;
  DAT_ffff0000be1b6fc4 = 0x30;
  DAT_ffff0000be1b6fcc = 4;
  DAT_ffff0000be1b6f88 = 0;
  DAT_ffff0000be1b6f80 = 0x200;
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b6fe0,0,0x17c);
  DAT_ffff0000be1b6fe0 = 0;
  DAT_ffff0000be1b7018 = 0x30;
  DAT_ffff0000be1b704c = 0x30;
  DAT_ffff0000be1b7054 = 4;
  DAT_ffff0000be1b705c = 4;
  DAT_ffff0000be1b70a8 = 4;
  FUN_ffff0000be091dc8();
  FUN_ffff0000be091e3c();
  lVar2 = FUN_ffff0000be091eb0(0x4000);
  if (lVar2 == 0) {
    uVar1 = 7;
  }
  else {
    uVar1 = FUN_ffff0000be088830(param_2,param_3,param_4,&local_38);
    if (uVar1 != 0) goto LAB_ffff0000be088b4c;
    uVar1 = FUN_ffff0000be091f34(param_1,lVar2,local_38,0x200);
    if (uVar1 != 0) goto LAB_ffff0000be088b4c;
    uVar1 = FUN_ffff0000be0877b0(lVar2,0x200,&DAT_ffff0000be1b6b48);
    if (uVar1 != 0) goto LAB_ffff0000be088b4c;
    if ((DAT_ffff0000be1b6b48 == 1) && (DAT_ffff0000be1b6b50 >> 1 == 0x1000000)) {
      if (DAT_ffff0000be1b6b4c + DAT_ffff0000be1b6b54 < 0x4001) {
        uVar1 = FUN_ffff0000be091f34(param_1,lVar2,local_38);
        if (uVar1 != 0) goto LAB_ffff0000be088b4c;
        local_38 = local_38 + (ulong)DAT_ffff0000be1b6b4c + (ulong)DAT_ffff0000be1b6b54;
        uVar1 = FUN_ffff0000be091f34(param_1,lVar2,local_38,0x200);
        if (uVar1 != 0) goto LAB_ffff0000be088b4c;
        uVar1 = FUN_ffff0000be0877b0(lVar2,0x200,&DAT_ffff0000be1b6b48);
        if (uVar1 != 0) goto LAB_ffff0000be088b4c;
        if (DAT_ffff0000be1b6b48 != 1) goto LAB_ffff0000be088c90;
        uVar1 = 0x10100d;
        if (DAT_ffff0000be1b6b50 != 0x2000002) goto LAB_ffff0000be088b4c;
        if (DAT_ffff0000be1b6b4c + DAT_ffff0000be1b6b54 < 0x4001) {
          uVar1 = FUN_ffff0000be091f34(param_1,lVar2,local_38);
          if (uVar1 == 0) {
            uVar1 = FUN_ffff0000be08b0b8(lVar2,DAT_ffff0000be1b6b54 + DAT_ffff0000be1b6b4c,0x1f400);
            if (uVar1 == 0) {
              FUN_ffff0000be05fd98
                        (s___s___s_cert_parsing_pass__ffff0000be0bc9ae,&DAT_ffff0000be0bb811,param_2
                        );
              FUN_ffff0000be091e74();
              return 0;
            }
          }
          goto LAB_ffff0000be088b4c;
        }
      }
      uVar1 = 0x101019;
      goto LAB_ffff0000be088b4c;
    }
LAB_ffff0000be088c90:
    uVar1 = 0x100d;
  }
  uVar1 = uVar1 | 0x100000;
LAB_ffff0000be088b4c:
  FUN_ffff0000be05fd98
            (s___s___s_cert_parsing_fail__0x_x__ffff0000be0c6cfb,&DAT_ffff0000be0bb811,param_2,uVar1
            );
  FUN_ffff0000be091e74();
  FUN_ffff0000be0894ac();
  return uVar1;
}


