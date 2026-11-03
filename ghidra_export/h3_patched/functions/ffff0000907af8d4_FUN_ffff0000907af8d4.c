// ffff0000907af8d4  size=904  FUN_ffff0000907af8d4

undefined8 FUN_ffff0000907af8d4(undefined8 param_1,undefined4 param_2,long param_3)

{
  int *piVar1;
  long *plVar2;
  undefined4 uVar3;
  int iVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  uVar3 = FUN_ffff0000907aa044();
  FUN_ffff00009077de54(s__SBC__sbc_en____d_ffff0000907fff36,uVar3);
  iVar4 = FUN_ffff0000907aa044();
  plVar2 = DAT_ffff000090839ad8;
  if (iVar4 == 0) {
    uVar5 = FUN_ffff0000907acfd8(*DAT_ffff000090839ad8,param_3,*(undefined4 *)(param_3 + 0x200));
    if ((int)uVar5 != 0) {
      FUN_ffff00009077de54(s_g_sec_oemkey___ffff0000907e8898);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar6 = 0;
        do {
          FUN_ffff00009077de54(s_0x_x_ffff0000907fe4a9,*(undefined1 *)(*plVar2 + uVar6));
          uVar6 = uVar6 + 1;
          if ((uVar6 & 0xf) == 0) {
            FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
          }
        } while (uVar6 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff00009077de54(s_pubk_>n___ffff0000907dbbd5);
      if (*(int *)(param_3 + 0x200) != 0) {
        uVar6 = 0;
        do {
          FUN_ffff00009077de54(s_0x_x_ffff0000907fe4a9,*(undefined1 *)(param_3 + uVar6));
          uVar6 = uVar6 + 1;
          if ((uVar6 & 0xf) == 0) {
            FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
          }
        } while (uVar6 < *(uint *)(param_3 + 0x200));
      }
      FUN_ffff00009077de54(s_pubk_auth_fail_ffff0000907fff49);
      uVar5 = 0x7019;
    }
  }
  else {
    FUN_ffff00009077de54(s__SBC__sbc_en___1_ffff0000907d06c6);
    piVar1 = DAT_ffff000090839ac8;
    uVar5 = FUN_ffff0000907a262c(param_1,param_2,0xffff00009090ce04,*DAT_ffff000090839ac8);
    if ((int)uVar5 == 0) {
      if (*piVar1 - 1U < 2) {
        uVar5 = FUN_ffff0000907aa068(0xffff00009090ce44,0x30,*DAT_ffff000090839b10);
        if (((int)uVar5 == 0) &&
           (uVar5 = FUN_ffff0000907acfd8(0xffff00009090ce04,0xffff00009090ce44,0x30),
           (int)uVar5 != 0)) {
          FUN_ffff00009077de54(s_pubk_auth_fail_ffff0000907fff49);
          FUN_ffff00009077de54(s_pubk_hash___ffff000090803325);
          uVar6 = 0;
          do {
            FUN_ffff00009077de54(s_0x_x_ffff0000907fe4a9,*(undefined1 *)(uVar6 - 0xffff6f6f31fc));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
            }
          } while (uVar6 != 0x30);
          FUN_ffff00009077de54(s_pubk_hash_fuse___ffff0000907defbb);
          uVar6 = 0;
          do {
            FUN_ffff00009077de54(s_0x_x_ffff0000907fe4a9,*(undefined1 *)(uVar6 - 0xffff6f6f31bc));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
            }
            uVar5 = 0x7019;
          } while (uVar6 != 0x30);
        }
      }
      else if (*piVar1 == 0) {
        uVar5 = FUN_ffff0000907aa068(0xffff00009090ce44,0x20,*DAT_ffff000090839b10);
        if ((((int)uVar5 == 0) &&
            (uVar5 = FUN_ffff0000907acfd8(0xffff00009090ce04,0xffff00009090ce44,0x20),
            (int)uVar5 != 0)) && (uVar5 = FUN_ffff0000907afc5c(param_1,param_2), (int)uVar5 != 0)) {
          FUN_ffff00009077de54(s_pubk_auth_fail_ffff0000907fff49);
          FUN_ffff00009077de54(s_pubk_hash___ffff000090803325);
          uVar6 = 0;
          do {
            FUN_ffff00009077de54(s_0x_x_ffff0000907fe4a9,*(undefined1 *)(uVar6 - 0xffff6f6f31fc));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
            }
          } while (uVar6 != 0x20);
          FUN_ffff00009077de54(s_pubk_hash_fuse___ffff0000907defbb);
          uVar6 = 0;
          do {
            FUN_ffff00009077de54(s_0x_x_ffff0000907fe4a9,*(undefined1 *)(uVar6 - 0xffff6f6f31bc));
            uVar6 = uVar6 + 1;
            if ((uVar6 & 0xf) == 0) {
              FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
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


