
void FUN_ffff0000be018860(ulong param_1)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  int iVar6;
  ulong uVar7;
  uint uVar8;
  
  FUN_ffff0000be01a590
            (DAT_ffff0000be108810 + (ulong)*(uint *)(DAT_ffff0000be108810 + 0xfc) * 0x78 + 0x50,
             0xffffffff);
  lVar4 = DAT_ffff0000be108810;
  uVar2 = (ulong)*(uint *)(DAT_ffff0000be108810 + 0xfc);
  *(undefined4 *)(DAT_ffff0000be108810 + uVar2 * 0x78 + 0x1c) = 0;
  if (param_1 == 0) {
LAB_ffff0000be018980:
    FUN_ffff0000be01a61c(lVar4 + uVar2 * 0x78 + 0x20,0);
  }
  else {
    uVar7 = 0;
    uVar8 = 0;
    do {
      if ((uVar8 >> 0x15 != 0) || (param_1 <= uVar7)) {
        FUN_ffff0000be01a61c(lVar4 + uVar2 * 0x78 + 0x20,0);
        uVar8 = *(uint *)(DAT_ffff0000be108810 + 0xfc) ^ 1;
        lVar4 = DAT_ffff0000be108810 + (ulong)uVar8 * 0x78;
        *(uint *)(DAT_ffff0000be108810 + 0xfc) = uVar8;
        FUN_ffff0000be01a590(lVar4 + 0x50,0xffffffff);
        lVar4 = DAT_ffff0000be108810;
        uVar2 = (ulong)*(uint *)(DAT_ffff0000be108810 + 0xfc);
        *(undefined4 *)(DAT_ffff0000be108810 + uVar2 * 0x78 + 0x1c) = 0;
        if (param_1 <= uVar7) goto LAB_ffff0000be018980;
        uVar8 = 0;
      }
      uVar3 = param_1 - uVar7;
      if (0x1fffff < uVar3) {
        uVar3 = 0x200000;
      }
      iVar1 = FUN_ffff0000be018bb8
                        (*(long *)(lVar4 + uVar2 * 0x78 + 0x10) + (ulong)uVar8,uVar3 & 0xffffffff);
      lVar4 = DAT_ffff0000be108810;
      if ((iVar1 < 0) || (iVar6 = (int)uVar3, iVar1 != iVar6)) {
        *(undefined4 *)(DAT_ffff0000be108810 + 0x160) = 0xffffff9a;
        FUN_ffff0000be05fd98(s_Read_usb_error__code_0x_x_ffff0000be0ab3eb,0xffffff9a);
        break;
      }
      uVar2 = (ulong)*(uint *)(DAT_ffff0000be108810 + 0xfc);
      iVar1 = *(int *)(DAT_ffff0000be108810 + 0x160);
      lVar5 = DAT_ffff0000be108810 + uVar2 * 0x78;
      *(int *)(lVar5 + 0x1c) = *(int *)(lVar5 + 0x1c) + iVar6;
      if (iVar1 < 0) break;
      uVar8 = uVar8 + iVar6;
      uVar7 = uVar3 + uVar7;
    } while (-1 < *(int *)(lVar4 + 0x164));
  }
  lVar4 = DAT_ffff0000be108810;
  FUN_ffff0000be01a61c(DAT_ffff0000be108810 + 0x20,0);
  FUN_ffff0000be01a61c(lVar4 + 0x98,0);
  FUN_ffff0000be01a61c(lVar4 + 0x130,0);
  return;
}


