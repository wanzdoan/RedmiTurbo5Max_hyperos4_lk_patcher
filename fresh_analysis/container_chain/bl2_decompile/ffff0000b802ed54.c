
undefined8 FUN_ffff0000b802ed54(int param_1)

{
  undefined4 *puVar1;
  undefined *puVar2;
  uint uVar3;
  long *plVar4;
  long lVar5;
  ulong uVar6;
  long lVar7;
  
  uVar3 = 0;
  if (param_1 != 0x2b) {
    uVar3 = (uint)(param_1 != 0x2d);
  }
  uVar6 = (ulong)uVar3;
  lVar5 = 1;
  if (param_1 != 0x2b) {
    lVar5 = 2;
  }
  puVar2 = &DAT_ffff0000b8125590 + uVar6 * 0xbd8;
  lVar5 = lVar5 - uVar6;
  plVar4 = (long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar6 * 8 + 0x10);
  do {
    lVar5 = lVar5 + -1;
    lVar7 = *plVar4;
    *(undefined4 *)(lVar7 + 8) = *(undefined4 *)(puVar2 + 0x14);
    *(undefined4 *)(lVar7 + 0x34) = *(undefined4 *)(puVar2 + 0x40);
    *(uint *)(lVar7 + 0x38) = *(uint *)(puVar2 + 0x44) & 0xffffffc3;
    *(undefined4 *)(lVar7 + 0x3c) = *(undefined4 *)(puVar2 + 0x48);
    *(undefined4 *)(lVar7 + 0x60) = *(undefined4 *)(puVar2 + 0x6c);
    *(undefined4 *)(lVar7 + 100) = *(undefined4 *)(puVar2 + 0x70);
    *(undefined4 *)(lVar7 + 0x68) = *(undefined4 *)(puVar2 + 0x74);
    *(undefined4 *)(lVar7 + 0x6c) = *(undefined4 *)(puVar2 + 0x78);
    *(undefined4 *)(lVar7 + 0x80) = *(undefined4 *)(puVar2 + 0x8c);
    *(undefined4 *)(lVar7 + 0x84) = *(undefined4 *)(puVar2 + 0x90);
    *(undefined4 *)(lVar7 + 0x88) = *(undefined4 *)(puVar2 + 0x94);
    *(undefined4 *)(lVar7 + 0x8c) = *(undefined4 *)(puVar2 + 0x98);
    *(undefined4 *)(lVar7 + 0x100) = *(undefined4 *)(puVar2 + 0x10c);
    *(undefined4 *)(lVar7 + 0x48) = *(undefined4 *)(puVar2 + 0x54);
    *(undefined4 *)(lVar7 + 0x600) = *(undefined4 *)(puVar2 + 0x60c);
    *(undefined4 *)(lVar7 + 0x604) = *(undefined4 *)(puVar2 + 0x610);
    *(undefined4 *)(lVar7 + 0x608) = *(undefined4 *)(puVar2 + 0x614);
    *(undefined4 *)(lVar7 + 0x60c) = *(undefined4 *)(puVar2 + 0x618);
    puVar1 = (undefined4 *)(puVar2 + 0x11c);
    puVar2 = puVar2 + 0xbd8;
    *(undefined4 *)(lVar7 + 0x110) = *puVar1;
    plVar4 = plVar4 + 1;
  } while (lVar5 != 0);
  return 0;
}


