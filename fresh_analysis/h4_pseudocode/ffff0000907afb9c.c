
undefined8 FUN_ffff0000907afb9c(undefined8 param_1,undefined4 param_2,long param_3)

{
  undefined *puVar1;
  undefined4 uVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  uVar2 = FUN_ffff0000907aa304();
  FUN_ffff00009077e11c(s__SBC__sbc_en____d_ffff0000908000de,uVar2);
  iVar3 = FUN_ffff0000907aa304();
  puVar1 = PTR_DAT_ffff000090839af8;
  if (iVar3 == 0) {
    uVar4 = FUN_ffff0000907ad298
                      (*(undefined8 *)PTR_DAT_ffff000090839af8,param_3,
                       *(undefined4 *)(param_3 + 0x200));
    if ((int)uVar4 != 0) {
      FUN_ffff00009077e11c(s_g_sec_oemkey___ffff0000907e89f3);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar5 = 0;
        do {
          FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(*(long *)puVar1 + uVar5));
          uVar5 = uVar5 + 1;
          if ((uVar5 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar5 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff00009077e11c(s_pubk_>n___ffff0000907dbcf7);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar5 = 0;
        do {
          FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(param_3 + uVar5));
          uVar5 = uVar5 + 1;
          if ((uVar5 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar5 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff00009077e11c(s_pubk_auth_fail_ffff0000908000f1);
      uVar4 = 0x7019;
    }
  }
  else {
    FUN_ffff00009077e11c(s__SBC__sbc_en___1_ffff0000907d06e5);
    puVar1 = PTR_DAT_ffff000090839ae8;
    uVar4 = FUN_ffff0000907a28ec
                      (param_1,param_2,&DAT_ffff00009090cec4,*(undefined4 *)PTR_DAT_ffff000090839ae8
                      );
    if ((int)uVar4 == 0) {
      if (*(int *)puVar1 - 1U < 2) {
        uVar4 = FUN_ffff0000907aa328
                          (&DAT_ffff00009090cf04,0x30,*(undefined4 *)PTR_DAT_ffff000090839b30);
        if (((int)uVar4 == 0) &&
           (uVar4 = FUN_ffff0000907ad298(&DAT_ffff00009090cec4,&DAT_ffff00009090cf04,0x30),
           (int)uVar4 != 0)) {
          FUN_ffff00009077e11c(s_pubk_auth_fail_ffff0000908000f1);
          FUN_ffff00009077e11c(s_pubk_hash___ffff00009080354d);
          uVar5 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,(&DAT_ffff00009090cec4)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
            }
          } while (uVar5 != 0x30);
          FUN_ffff00009077e11c(s_pubk_hash_fuse___ffff0000907df075);
          uVar5 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,(&DAT_ffff00009090cf04)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
            }
            uVar4 = 0x7019;
          } while (uVar5 != 0x30);
        }
      }
      else if (*(int *)puVar1 == 0) {
        uVar4 = FUN_ffff0000907aa328
                          (&DAT_ffff00009090cf04,0x20,*(undefined4 *)PTR_DAT_ffff000090839b30);
        if ((((int)uVar4 == 0) &&
            (uVar4 = FUN_ffff0000907ad298(&DAT_ffff00009090cec4,&DAT_ffff00009090cf04,0x20),
            (int)uVar4 != 0)) && (uVar4 = FUN_ffff0000907aff24(param_1,param_2), (int)uVar4 != 0)) {
          FUN_ffff00009077e11c(s_pubk_auth_fail_ffff0000908000f1);
          FUN_ffff00009077e11c(s_pubk_hash___ffff00009080354d);
          uVar5 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,(&DAT_ffff00009090cec4)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
            }
          } while (uVar5 != 0x20);
          FUN_ffff00009077e11c(s_pubk_hash_fuse___ffff0000907df075);
          uVar5 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,(&DAT_ffff00009090cf04)[uVar5]);
            uVar5 = uVar5 + 1;
            if ((uVar5 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
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


