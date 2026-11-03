
uint FUN_ffff0000b806af68
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4,
               long param_5)

{
  uint uVar1;
  long lVar2;
  long local_38;
  
  local_38 = param_5;
  FUN_ffff0000b8073140();
  DAT_ffff0000b81076e4 = 0x100;
  DAT_ffff0000b81076e8 = 0x100;
  DAT_ffff0000b81b8804 = 0;
  DAT_ffff0000b81b8808 = 0;
  DAT_ffff0000b81076ec = 0x20;
  DAT_ffff0000b81b880c = 0;
  DAT_ffff0000b81b8810 = 0;
  DAT_ffff0000b81b8814 = 0;
  FUN_ffff0000b804d070(&DAT_ffff0000b81b8848,0,0x34);
  FUN_ffff0000b804d070(&DAT_ffff0000b81b8888,0,0x34);
  DAT_ffff0000b81b8800 = DAT_ffff0000b81b8800 + 1;
  FUN_ffff0000b804d070(&DAT_ffff0000b81b88bc,0,0x464);
  DAT_ffff0000b81b8abc = 0x200;
  DAT_ffff0000b81b8d04 = 0x30;
  DAT_ffff0000b81b8d0c = 4;
  DAT_ffff0000b81b8cc8 = 0;
  DAT_ffff0000b81b8cc0 = 0x200;
  FUN_ffff0000b804d070(&DAT_ffff0000b81b8d20,0,0x17c);
  DAT_ffff0000b81b8d20 = 0;
  DAT_ffff0000b81b8d58 = 0x30;
  DAT_ffff0000b81b8d8c = 0x30;
  DAT_ffff0000b81b8d94 = 4;
  DAT_ffff0000b81b8d9c = 4;
  DAT_ffff0000b81b8de8 = 4;
  FUN_ffff0000b807316c();
  FUN_ffff0000b80731e0();
  lVar2 = FUN_ffff0000b8073254(0x4000);
  if (lVar2 == 0) {
    uVar1 = 7;
  }
  else {
    uVar1 = FUN_ffff0000b806ae34(param_2,param_3,param_4,&local_38);
    if (uVar1 != 0) goto LAB_ffff0000b806b150;
    uVar1 = FUN_ffff0000b80732d8(param_1,lVar2,local_38,0x200);
    if (uVar1 != 0) goto LAB_ffff0000b806b150;
    uVar1 = FUN_ffff0000b8069db4(lVar2,0x200,&DAT_ffff0000b81b8888);
    if (uVar1 != 0) goto LAB_ffff0000b806b150;
    if ((DAT_ffff0000b81b8888 == 1) && (DAT_ffff0000b81b8890 >> 1 == 0x1000000)) {
      if (DAT_ffff0000b81b888c + DAT_ffff0000b81b8894 < 0x4001) {
        uVar1 = FUN_ffff0000b80732d8(param_1,lVar2,local_38);
        if (uVar1 != 0) goto LAB_ffff0000b806b150;
        local_38 = local_38 + (ulong)DAT_ffff0000b81b888c + (ulong)DAT_ffff0000b81b8894;
        uVar1 = FUN_ffff0000b80732d8(param_1,lVar2,local_38,0x200);
        if (uVar1 != 0) goto LAB_ffff0000b806b150;
        uVar1 = FUN_ffff0000b8069db4(lVar2,0x200,&DAT_ffff0000b81b8888);
        if (uVar1 != 0) goto LAB_ffff0000b806b150;
        if (DAT_ffff0000b81b8888 != 1) goto LAB_ffff0000b806b294;
        uVar1 = 0x10100d;
        if (DAT_ffff0000b81b8890 != 0x2000002) goto LAB_ffff0000b806b150;
        if (DAT_ffff0000b81b888c + DAT_ffff0000b81b8894 < 0x4001) {
          uVar1 = FUN_ffff0000b80732d8(param_1,lVar2,local_38);
          if (uVar1 == 0) {
            uVar1 = FUN_ffff0000b806d6bc(lVar2,DAT_ffff0000b81b8894 + DAT_ffff0000b81b888c,0x1f400);
            if (uVar1 == 0) {
              FUN_ffff0000b804c33c
                        (s___s___s_cert_parsing_pass__ffff0000b8099636,&DAT_ffff0000b80987e6,param_2
                        );
              FUN_ffff0000b8073218();
              return 0;
            }
          }
          goto LAB_ffff0000b806b150;
        }
      }
      uVar1 = 0x101019;
      goto LAB_ffff0000b806b150;
    }
LAB_ffff0000b806b294:
    uVar1 = 0x100d;
  }
  uVar1 = uVar1 | 0x100000;
LAB_ffff0000b806b150:
  FUN_ffff0000b804c33c
            (s___s___s_cert_parsing_fail__0x_x__ffff0000b80a186d,&DAT_ffff0000b80987e6,param_2,uVar1
            );
  FUN_ffff0000b8073218();
  FUN_ffff0000b806bab0();
  return uVar1;
}


