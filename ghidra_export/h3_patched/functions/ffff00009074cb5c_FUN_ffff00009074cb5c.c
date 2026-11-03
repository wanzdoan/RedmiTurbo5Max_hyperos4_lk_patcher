// ffff00009074cb5c  size=240  FUN_ffff00009074cb5c

undefined8 FUN_ffff00009074cb5c(int param_1)

{
  long lVar1;
  uint uVar2;
  long *plVar3;
  long lVar4;
  ulong uVar5;
  long lVar6;
  
  uVar2 = 0;
  if (param_1 != 0x2b) {
    uVar2 = (uint)(param_1 != 0x2d);
  }
  uVar5 = (ulong)uVar2;
  lVar4 = 1;
  if (param_1 != 0x2b) {
    lVar4 = 2;
  }
  lVar1 = uVar5 * 0xbd8 + -0xffff6f788448;
  lVar4 = lVar4 - uVar5;
  plVar3 = (long *)(s_0123456789abcdef_ffff000090804a48 + uVar5 * 8 + 0x10);
  do {
    lVar6 = *plVar3;
    lVar4 = lVar4 + -1;
    *(undefined4 *)(lVar1 + 0x14) = *(undefined4 *)(lVar6 + 8);
    *(undefined4 *)(lVar1 + 0x40) = *(undefined4 *)(lVar6 + 0x34);
    *(undefined4 *)(lVar1 + 0x44) = *(undefined4 *)(lVar6 + 0x38);
    *(undefined4 *)(lVar1 + 0x48) = *(undefined4 *)(lVar6 + 0x3c);
    *(undefined4 *)(lVar1 + 0x6c) = *(undefined4 *)(lVar6 + 0x60);
    *(undefined4 *)(lVar1 + 0x70) = *(undefined4 *)(lVar6 + 100);
    *(undefined4 *)(lVar1 + 0x74) = *(undefined4 *)(lVar6 + 0x68);
    *(undefined4 *)(lVar1 + 0x78) = *(undefined4 *)(lVar6 + 0x6c);
    *(undefined4 *)(lVar1 + 0x8c) = *(undefined4 *)(lVar6 + 0x80);
    *(undefined4 *)(lVar1 + 0x90) = *(undefined4 *)(lVar6 + 0x84);
    *(undefined4 *)(lVar1 + 0x94) = *(undefined4 *)(lVar6 + 0x88);
    *(undefined4 *)(lVar1 + 0x98) = *(undefined4 *)(lVar6 + 0x8c);
    *(undefined4 *)(lVar1 + 0x10c) = *(undefined4 *)(lVar6 + 0x100);
    *(undefined4 *)(lVar1 + 0x54) = *(undefined4 *)(lVar6 + 0x48);
    *(undefined4 *)(lVar1 + 0x60c) = *(undefined4 *)(lVar6 + 0x600);
    *(undefined4 *)(lVar1 + 0x610) = *(undefined4 *)(lVar6 + 0x604);
    *(undefined4 *)(lVar1 + 0x614) = *(undefined4 *)(lVar6 + 0x608);
    *(undefined4 *)(lVar1 + 0x618) = *(undefined4 *)(lVar6 + 0x60c);
    *(undefined4 *)(lVar1 + 0x11c) = *(undefined4 *)(lVar6 + 0x110);
    lVar1 = lVar1 + 0xbd8;
    plVar3 = plVar3 + 1;
  } while (lVar4 != 0);
  return 0;
}


