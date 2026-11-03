
uint FUN_ffff0000b806aa80
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4,
               long param_5,undefined4 param_6)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  long local_48;
  
  local_48 = param_5;
  FUN_ffff0000b8073140();
  DAT_ffff0000b81b8804 = 0;
  DAT_ffff0000b81076e4 = 0x100;
  DAT_ffff0000b81076e8 = 0x100;
  DAT_ffff0000b81076ec = 0x20;
  DAT_ffff0000b81b8808 = 0;
  DAT_ffff0000b81b880c = 0;
  DAT_ffff0000b81b8880 = (code *)PTR_FUN_ffff0000b8108848;
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
  lVar3 = FUN_ffff0000b8073254(0x4000);
  if (lVar3 == 0) {
    uVar1 = 7;
  }
  else {
    uVar1 = FUN_ffff0000b806ae34(param_2,param_3,param_4,&local_48);
    if (uVar1 != 0) goto LAB_ffff0000b806ac74;
    uVar1 = (*DAT_ffff0000b81b8880)(param_1,lVar3,local_48,0x200);
    if (uVar1 != 0) goto LAB_ffff0000b806ac74;
    uVar1 = FUN_ffff0000b8069db4(lVar3,0x200,&DAT_ffff0000b81b8888);
    if (uVar1 != 0) goto LAB_ffff0000b806ac74;
    if ((DAT_ffff0000b81b8888 == 1) && (DAT_ffff0000b81b8890 >> 1 == 0x1000000)) {
      if (DAT_ffff0000b81b888c + DAT_ffff0000b81b8894 < 0x4001) {
        uVar1 = (*DAT_ffff0000b81b8880)(param_1,lVar3,local_48);
        if (uVar1 != 0) goto LAB_ffff0000b806ac74;
        uVar1 = FUN_ffff0000b806e3dc
                          (lVar3,DAT_ffff0000b81b8894 + DAT_ffff0000b81b888c,&DAT_ffff0000b81b88bc);
        if (uVar1 != 0) goto LAB_ffff0000b806ac74;
        iVar2 = FUN_ffff0000b8069da4(DAT_ffff0000b81b8850);
        if ((iVar2 == 0x1000000) && (iVar2 = FUN_ffff0000b8067ce8(DAT_ffff0000b81b8850), iVar2 != 0)
           ) {
          FUN_ffff0000b804c33c(s_fail_to_set_MD_pubk_hash_ffff0000b809bffb);
        }
        local_48 = local_48 + (ulong)DAT_ffff0000b81b888c + (ulong)DAT_ffff0000b81b8894;
        uVar1 = (*DAT_ffff0000b81b8880)(param_1,lVar3,local_48,0x200);
        if (uVar1 != 0) goto LAB_ffff0000b806ac74;
        uVar1 = FUN_ffff0000b8069db4(lVar3,0x200,&DAT_ffff0000b81b8888);
        if (uVar1 != 0) goto LAB_ffff0000b806ac74;
        if (DAT_ffff0000b81b8888 != 1) goto LAB_ffff0000b806ae2c;
        uVar1 = 0x10100d;
        if (DAT_ffff0000b81b8890 != 0x2000002) goto LAB_ffff0000b806ac74;
        if (DAT_ffff0000b81b888c + DAT_ffff0000b81b8894 < 0x4001) {
          uVar1 = (*DAT_ffff0000b81b8880)(param_1,lVar3,local_48);
          if (uVar1 == 0) {
            uVar1 = FUN_ffff0000b806edac
                              (lVar3,DAT_ffff0000b81b8894 + DAT_ffff0000b81b888c,
                               &DAT_ffff0000b81b88bc,&DAT_ffff0000b81b8d20,param_6);
            if (uVar1 == 0) {
              FUN_ffff0000b804c33c
                        (s___s___s_cert_chain_vfy_pass_ffff0000b809e066,&DAT_ffff0000b80987e6,
                         param_2);
              FUN_ffff0000b8073218();
              return 0;
            }
          }
          goto LAB_ffff0000b806ac74;
        }
      }
      uVar1 = 0x101019;
      goto LAB_ffff0000b806ac74;
    }
LAB_ffff0000b806ae2c:
    uVar1 = 0x100d;
  }
  uVar1 = uVar1 | 0x100000;
LAB_ffff0000b806ac74:
  FUN_ffff0000b804c33c
            (s___s___s_cert_chain_vfy_fail__0x__ffff0000b809e083,&DAT_ffff0000b80987e6,param_2,uVar1
            );
  FUN_ffff0000b8073218();
  FUN_ffff0000b806bab0();
  FUN_ffff0000b8073158();
  return uVar1;
}


