
uint FUN_ffff0000be08847c
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4,
               long param_5,undefined4 param_6)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  long local_48;
  
  local_48 = param_5;
  FUN_ffff0000be091d9c();
  DAT_ffff0000be1b6ac4 = 0;
  DAT_ffff0000be0f4094 = 0x100;
  DAT_ffff0000be0f4098 = 0x100;
  DAT_ffff0000be0f409c = 0x20;
  DAT_ffff0000be1b6ac8 = 0;
  DAT_ffff0000be1b6acc = 0;
  DAT_ffff0000be1b6b40 = (code *)PTR_FUN_ffff0000be0f5778;
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
  lVar3 = FUN_ffff0000be091eb0(0x4000);
  if (lVar3 == 0) {
    uVar1 = 7;
  }
  else {
    uVar1 = FUN_ffff0000be088830(param_2,param_3,param_4,&local_48);
    if (uVar1 != 0) goto LAB_ffff0000be088670;
    uVar1 = (*DAT_ffff0000be1b6b40)(param_1,lVar3,local_48,0x200);
    if (uVar1 != 0) goto LAB_ffff0000be088670;
    uVar1 = FUN_ffff0000be0877b0(lVar3,0x200,&DAT_ffff0000be1b6b48);
    if (uVar1 != 0) goto LAB_ffff0000be088670;
    if ((DAT_ffff0000be1b6b48 == 1) && (DAT_ffff0000be1b6b50 >> 1 == 0x1000000)) {
      if (DAT_ffff0000be1b6b4c + DAT_ffff0000be1b6b54 < 0x4001) {
        uVar1 = (*DAT_ffff0000be1b6b40)(param_1,lVar3,local_48);
        if (uVar1 != 0) goto LAB_ffff0000be088670;
        uVar1 = FUN_ffff0000be08bdd8
                          (lVar3,DAT_ffff0000be1b6b54 + DAT_ffff0000be1b6b4c,&DAT_ffff0000be1b6b7c);
        if (uVar1 != 0) goto LAB_ffff0000be088670;
        iVar2 = FUN_ffff0000be0877a0(DAT_ffff0000be1b6b10);
        if ((iVar2 == 0x1000000) && (iVar2 = FUN_ffff0000be0856a8(DAT_ffff0000be1b6b10), iVar2 != 0)
           ) {
          FUN_ffff0000be05fd98(s_fail_to_set_MD_pubk_hash_ffff0000be0c01bb);
        }
        local_48 = local_48 + (ulong)DAT_ffff0000be1b6b4c + (ulong)DAT_ffff0000be1b6b54;
        uVar1 = (*DAT_ffff0000be1b6b40)(param_1,lVar3,local_48,0x200);
        if (uVar1 != 0) goto LAB_ffff0000be088670;
        uVar1 = FUN_ffff0000be0877b0(lVar3,0x200,&DAT_ffff0000be1b6b48);
        if (uVar1 != 0) goto LAB_ffff0000be088670;
        if (DAT_ffff0000be1b6b48 != 1) goto LAB_ffff0000be088828;
        uVar1 = 0x10100d;
        if (DAT_ffff0000be1b6b50 != 0x2000002) goto LAB_ffff0000be088670;
        if (DAT_ffff0000be1b6b4c + DAT_ffff0000be1b6b54 < 0x4001) {
          uVar1 = (*DAT_ffff0000be1b6b40)(param_1,lVar3,local_48);
          if (uVar1 == 0) {
            uVar1 = FUN_ffff0000be08c7a8
                              (lVar3,DAT_ffff0000be1b6b54 + DAT_ffff0000be1b6b4c,
                               &DAT_ffff0000be1b6b7c,&DAT_ffff0000be1b6fe0,param_6);
            if (uVar1 == 0) {
              FUN_ffff0000be05fd98
                        (s___s___s_cert_chain_vfy_pass_ffff0000be0c2693,&DAT_ffff0000be0bb811,
                         param_2);
              FUN_ffff0000be091e74();
              return 0;
            }
          }
          goto LAB_ffff0000be088670;
        }
      }
      uVar1 = 0x101019;
      goto LAB_ffff0000be088670;
    }
LAB_ffff0000be088828:
    uVar1 = 0x100d;
  }
  uVar1 = uVar1 | 0x100000;
LAB_ffff0000be088670:
  FUN_ffff0000be05fd98
            (s___s___s_cert_chain_vfy_fail__0x__ffff0000be0c26b0,&DAT_ffff0000be0bb811,param_2,uVar1
            );
  FUN_ffff0000be091e74();
  FUN_ffff0000be0894ac();
  FUN_ffff0000be091db4();
  return uVar1;
}


