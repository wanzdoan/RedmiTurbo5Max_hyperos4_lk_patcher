
undefined8 FUN_ffff0000be089d90(undefined8 param_1,undefined4 param_2,long param_3)

{
  undefined *puVar1;
  undefined4 uVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  uVar2 = FUN_ffff0000be0852f8();
  FUN_ffff0000be05fd98(s__SBC__sbc_en____d_ffff0000be0c4aa7,uVar2);
  iVar3 = FUN_ffff0000be0852f8();
  puVar1 = PTR_DAT_ffff0000be0f5760;
  if (iVar3 == 0) {
    uVar4 = FUN_ffff0000be08748c
                      (*(undefined8 *)PTR_DAT_ffff0000be0f5760,param_3,
                       *(undefined4 *)(param_3 + 0x200));
    if ((int)uVar4 != 0) {
      FUN_ffff0000be05fd98(s_g_sec_oemkey___ffff0000be0b4c52);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar5 = 0;
        do {
          FUN_ffff0000be05fd98(s_0x_x_ffff0000be0c390e,*(undefined1 *)(*(long *)puVar1 + uVar5));
          uVar5 = uVar5 + 1;
          if ((uVar5 & 0xf) == 0) {
            FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
          }
        } while (uVar5 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff0000be05fd98(s_pubk_>n___ffff0000be0ac149);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar5 = 0;
        do {
          FUN_ffff0000be05fd98(s_0x_x_ffff0000be0c390e,*(undefined1 *)(param_3 + uVar5));
          uVar5 = uVar5 + 1;
          if ((uVar5 & 0xf) == 0) {
            FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
          }
        } while (uVar5 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff0000be05fd98(s_pubk_auth_fail_ffff0000be0c4aba);
      uVar4 = 0x7019;
    }
  }
  else {
    FUN_ffff0000be05fd98(s__SBC__sbc_en___1_ffff0000be0a3f8a);
    puVar1 = PTR_DAT_ffff0000be0f5750;
    uVar4 = FUN_ffff0000be07efb4
                      (param_1,param_2,&DAT_ffff0000be1b7384,*(undefined4 *)PTR_DAT_ffff0000be0f5750
                      );
    if ((int)uVar4 == 0) {
      if (*(int *)puVar1 - 1U < 2) {
        uVar4 = FUN_ffff0000be08531c
                          (&DAT_ffff0000be1b73c4,0x30,*(undefined4 *)PTR_DAT_ffff0000be0f5798);
        if (((int)uVar4 == 0) &&
           (uVar4 = FUN_ffff0000be08748c(&DAT_ffff0000be1b7384,&DAT_ffff0000be1b73c4,0x30),
           (int)uVar4 != 0)) {
          FUN_ffff0000be05fd98(s_pubk_auth_fail_ffff0000be0c4aba);
          FUN_ffff0000be05fd98(s_pubk_hash___ffff0000be0c6d56);
          uVar5 = 0;
          do {
            FUN_ffff0000be05fd98(s_0x_x_ffff0000be0c390e,(&DAT_ffff0000be1b7384)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
            }
          } while (uVar5 != 0x30);
          FUN_ffff0000be05fd98(s_pubk_hash_fuse___ffff0000be0ae158);
          uVar5 = 0;
          do {
            FUN_ffff0000be05fd98(s_0x_x_ffff0000be0c390e,(&DAT_ffff0000be1b73c4)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
            }
            uVar4 = 0x7019;
          } while (uVar5 != 0x30);
        }
      }
      else if (*(int *)puVar1 == 0) {
        uVar4 = FUN_ffff0000be08531c
                          (&DAT_ffff0000be1b73c4,0x20,*(undefined4 *)PTR_DAT_ffff0000be0f5798);
        if ((((int)uVar4 == 0) &&
            (uVar4 = FUN_ffff0000be08748c(&DAT_ffff0000be1b7384,&DAT_ffff0000be1b73c4,0x20),
            (int)uVar4 != 0)) && (uVar4 = FUN_ffff0000be08a118(param_1,param_2), (int)uVar4 != 0)) {
          FUN_ffff0000be05fd98(s_pubk_auth_fail_ffff0000be0c4aba);
          FUN_ffff0000be05fd98(s_pubk_hash___ffff0000be0c6d56);
          uVar5 = 0;
          do {
            FUN_ffff0000be05fd98(s_0x_x_ffff0000be0c390e,(&DAT_ffff0000be1b7384)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
            }
          } while (uVar5 != 0x20);
          FUN_ffff0000be05fd98(s_pubk_hash_fuse___ffff0000be0ae158);
          uVar5 = 0;
          do {
            FUN_ffff0000be05fd98(s_0x_x_ffff0000be0c390e,(&DAT_ffff0000be1b73c4)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
            }
            uVar4 = 0x7019;
          } while (uVar5 != 0x20);
        }
      }
      else {
        uVar4 = 0x101023;
      }
    }
  }
  return uVar4;
}


