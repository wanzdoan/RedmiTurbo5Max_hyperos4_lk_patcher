// ffff00009070f610  size=400  FUN_ffff00009070f610

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070f610(ulong param_1)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  int iVar6;
  ulong uVar7;
  uint uVar8;
  
  FUN_ffff000090717598
            (_DAT_ffff00009083e0a0 + (ulong)*(uint *)(_DAT_ffff00009083e0a0 + 0xfc) * 0x78 + 0x50,
             0xffffffff);
  lVar4 = _DAT_ffff00009083e0a0;
  uVar2 = (ulong)*(uint *)(_DAT_ffff00009083e0a0 + 0xfc);
  *(undefined4 *)(_DAT_ffff00009083e0a0 + uVar2 * 0x78 + 0x1c) = 0;
  if (param_1 == 0) {
LAB_ffff00009070f730:
    FUN_ffff000090717624(lVar4 + uVar2 * 0x78 + 0x20,0);
  }
  else {
    uVar7 = 0;
    uVar8 = 0;
    do {
      if ((uVar8 >> 0x15 != 0) || (param_1 <= uVar7)) {
        FUN_ffff000090717624(lVar4 + uVar2 * 0x78 + 0x20,0);
        uVar8 = *(uint *)(_DAT_ffff00009083e0a0 + 0xfc) ^ 1;
        lVar4 = _DAT_ffff00009083e0a0 + (ulong)uVar8 * 0x78;
        *(uint *)(_DAT_ffff00009083e0a0 + 0xfc) = uVar8;
        FUN_ffff000090717598(lVar4 + 0x50,0xffffffff);
        lVar4 = _DAT_ffff00009083e0a0;
        uVar2 = (ulong)*(uint *)(_DAT_ffff00009083e0a0 + 0xfc);
        *(undefined4 *)(_DAT_ffff00009083e0a0 + uVar2 * 0x78 + 0x1c) = 0;
        if (param_1 <= uVar7) goto LAB_ffff00009070f730;
        uVar8 = 0;
      }
      uVar3 = param_1 - uVar7;
      if (0x1fffff < uVar3) {
        uVar3 = 0x200000;
      }
      iVar1 = FUN_ffff00009070f968
                        (*(long *)(lVar4 + uVar2 * 0x78 + 0x10) + (ulong)uVar8,uVar3 & 0xffffffff);
      lVar4 = _DAT_ffff00009083e0a0;
      if ((iVar1 < 0) || (iVar6 = (int)uVar3, iVar1 != iVar6)) {
        *(undefined4 *)(_DAT_ffff00009083e0a0 + 0x160) = 0xffffff9a;
        FUN_ffff00009077e11c(s_Read_usb_error__code_0x_x_ffff0000907da591,0xffffff9a);
        break;
      }
      uVar2 = (ulong)*(uint *)(_DAT_ffff00009083e0a0 + 0xfc);
      iVar1 = *(int *)(_DAT_ffff00009083e0a0 + 0x160);
      lVar5 = _DAT_ffff00009083e0a0 + uVar2 * 0x78;
      *(int *)(lVar5 + 0x1c) = *(int *)(lVar5 + 0x1c) + iVar6;
      if (iVar1 < 0) break;
      uVar8 = uVar8 + iVar6;
      uVar7 = uVar3 + uVar7;
    } while (-1 < *(int *)(lVar4 + 0x164));
  }
  lVar4 = _DAT_ffff00009083e0a0;
  FUN_ffff000090717624(_DAT_ffff00009083e0a0 + 0x20,0);
  FUN_ffff000090717624(lVar4 + 0x98,0);
  FUN_ffff000090717624(lVar4 + 0x130,0);
  return;
}


