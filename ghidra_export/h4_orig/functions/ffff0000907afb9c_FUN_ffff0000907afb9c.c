// ffff0000907afb9c  size=904  FUN_ffff0000907afb9c

undefined8 FUN_ffff0000907afb9c(undefined8 param_1,undefined4 param_2,long param_3)

{
  int *piVar1;
  long *plVar2;
  undefined4 uVar3;
  int iVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  uVar3 = FUN_ffff0000907aa304();
  FUN_ffff00009077e11c(s__SBC__sbc_en____d_ffff0000908000de,uVar3);
  iVar4 = FUN_ffff0000907aa304();
  plVar2 = DAT_ffff000090839af8;
  if (iVar4 == 0) {
    uVar5 = FUN_ffff0000907ad298(*DAT_ffff000090839af8,param_3,*(undefined4 *)(param_3 + 0x200));
    if ((int)uVar5 != 0) {
      FUN_ffff00009077e11c(s_g_sec_oemkey___ffff0000907e89f3);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar6 = 0;
        do {
          FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(*plVar2 + uVar6));
          uVar6 = uVar6 + 1;
          if ((uVar6 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar6 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff00009077e11c(s_pubk_>n___ffff0000907dbcf7);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar6 = 0;
        do {
          FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(param_3 + uVar6));
          uVar6 = uVar6 + 1;
          if ((uVar6 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar6 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff00009077e11c(s_pubk_auth_fail_ffff0000908000f1);
      uVar5 = 0x7019;
    }
  }
  else {
    FUN_ffff00009077e11c(s__SBC__sbc_en___1_ffff0000907d06e5);
    piVar1 = DAT_ffff000090839ae8;
    uVar5 = FUN_ffff0000907a28ec(param_1,param_2,0xffff00009090cec4,*DAT_ffff000090839ae8);
    if ((int)uVar5 == 0) {
      if (*piVar1 - 1U < 2) {
        uVar5 = FUN_ffff0000907aa328(0xffff00009090cf04,0x30,*DAT_ffff000090839b30);
        if (((int)uVar5 == 0) &&
           (uVar5 = FUN_ffff0000907ad298(0xffff00009090cec4,0xffff00009090cf04,0x30),
           (int)uVar5 != 0)) {
          FUN_ffff00009077e11c(s_pubk_auth_fail_ffff0000908000f1);
          FUN_ffff00009077e11c(s_pubk_hash___ffff00009080354d);
          uVar6 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(uVar6 - 0xffff6f6f313c));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
            }
          } while (uVar6 != 0x30);
          FUN_ffff00009077e11c(s_pubk_hash_fuse___ffff0000907df075);
          uVar6 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(uVar6 - 0xffff6f6f30fc));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
            }
            uVar5 = 0x7019;
          } while (uVar6 != 0x30);
        }
      }
      else if (*piVar1 == 0) {
        uVar5 = FUN_ffff0000907aa328(0xffff00009090cf04,0x20,*DAT_ffff000090839b30);
        if ((((int)uVar5 == 0) &&
            (uVar5 = FUN_ffff0000907ad298(0xffff00009090cec4,0xffff00009090cf04,0x20),
            (int)uVar5 != 0)) && (uVar5 = FUN_ffff0000907aff24(param_1,param_2), (int)uVar5 != 0)) {
          FUN_ffff00009077e11c(s_pubk_auth_fail_ffff0000908000f1);
          FUN_ffff00009077e11c(s_pubk_hash___ffff00009080354d);
          uVar6 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(uVar6 - 0xffff6f6f313c));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
            }
          } while (uVar6 != 0x20);
          FUN_ffff00009077e11c(s_pubk_hash_fuse___ffff0000907df075);
          uVar6 = 0;
          do {
            FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)(uVar6 - 0xffff6f6f30fc));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
            }
            uVar5 = 0x7019;
          } while (uVar6 != 0x20);
        }
      }
      else {
        uVar5 = 0x101023;
      }
    }
  }
  return uVar5;
}


