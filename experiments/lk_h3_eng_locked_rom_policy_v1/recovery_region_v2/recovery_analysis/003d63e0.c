
/* ScreenRecoveryUI::CheckBackgroundTextImages() */

void __thiscall ScreenRecoveryUI::CheckBackgroundTextImages(ScreenRecoveryUI *this)

{
  ulong uVar1;
  long lVar2;
  byte *pbVar3;
  byte *pbVar4;
  int iVar5;
  ulong uVar6;
  byte *pbVar7;
  string local_80;
  char acStack_7f [15];
  void *local_70;
  byte *local_60;
  byte *local_58;
  undefined8 local_50;
  long local_48;

  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  local_80 = (string)0x1e;
  local_60 = (byte *)0x0;
  local_58 = (byte *)0x0;
  local_50 = 0;
  local_70 = (void *)((ulong)local_70 & 0xffffffffffffff00);
  acStack_7f._0_7_ = s_installing_text_0015d458._0_7_;
  acStack_7f[7] = s_installing_text_0015d458[7];
  acStack_7f._8_7_ = s_installing_text_0015d458._8_7_;
  get_locales_in_png(&local_80);
  if (((byte)local_80 & 1) != 0) {
    operator_delete(local_70);
  }
  if (local_60 == local_58) {
    (**(code **)(*(long *)this + 0x158))(this,"Failed to load locales from the resource files\n");
  }
  else {
    local_80 = (string)0x0;
    acStack_7f[0] = '\0';
    acStack_7f[1] = '\0';
    acStack_7f[2] = '\0';
    acStack_7f[3] = '\0';
    acStack_7f[4] = '\0';
    acStack_7f[5] = '\0';
    acStack_7f[6] = '\0';
    acStack_7f[7] = '\0';
    acStack_7f[8] = '\0';
    acStack_7f[9] = '\0';
    acStack_7f[10] = '\0';
    acStack_7f[0xb] = '\0';
    acStack_7f[0xc] = '\0';
    acStack_7f[0xd] = '\0';
    acStack_7f[0xe] = '\0';
    local_70 = (void *)0x0;
    if (((byte)this[0x9c0] & 1) == 0) {
      local_70 = *(void **)(this + 0x9d0);
      acStack_7f[7] = (char)*(undefined8 *)(this + 0x9c8);
      acStack_7f._8_7_ = (undefined7)((ulong)*(undefined8 *)(this + 0x9c8) >> 8);
      local_80 = SUB81(*(undefined8 *)(this + 0x9c0),0);
      acStack_7f._0_7_ = (undefined7)((ulong)*(undefined8 *)(this + 0x9c0) >> 8);
    }
    else {
      std::string::__init_copy_ctor_external
                (&local_80,*(char **)(this + 0x9d0),*(ulong *)(this + 0x9c8));
    }
    SelectAndShowBackgroundText(this,(vector *)&local_60,0);
    (**(code **)(*(long *)this + 0x1a8))(this);
    uVar6 = 0;
LAB_003d64e0:
    do {
      while (iVar5 = (**(code **)(*(long *)this + 0x170))(this), iVar5 < 0x6c) {
        if (iVar5 == -2 || iVar5 == 0x1c) goto LAB_003d65a0;
        if (iVar5 == 0x67) {
LAB_003d6574:
          if (uVar6 == 0) {
            uVar6 = ((long)local_58 - (long)local_60 >> 3) * -0x5555555555555555;
          }
          uVar6 = uVar6 - 1;
          SelectAndShowBackgroundText(this,(vector *)&local_60,uVar6);
        }
      }
      if (iVar5 < 0x73) {
        if ((iVar5 == 0x6c) || (iVar5 == 0x72)) {
          uVar1 = 0;
          if (uVar6 != ((long)local_58 - (long)local_60 >> 3) * -0x5555555555555555 - 1U) {
            uVar1 = uVar6 + 1;
          }
          SelectAndShowBackgroundText(this,(vector *)&local_60,uVar1);
          uVar6 = uVar1;
        }
        goto LAB_003d64e0;
      }
      if (iVar5 == 0x73) goto LAB_003d6574;
    } while (iVar5 != 0x74);
LAB_003d65a0:
    SetLocale(this,&local_80);
    if (((byte)local_80 & 1) != 0) {
      operator_delete(local_70);
    }
  }
  pbVar4 = local_60;
  pbVar7 = local_58;
  if (local_60 != (byte *)0x0) {
    while (pbVar3 = pbVar7, pbVar3 != pbVar4) {
      pbVar7 = pbVar3 + -0x18;
      if ((*pbVar7 & 1) != 0) {
        operator_delete(*(void **)(pbVar3 + -8));
      }
    }
    local_58 = pbVar4;
    operator_delete(local_60);
  }
  if (*(long *)(lVar2 + 0x28) != local_48) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}
