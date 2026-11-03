// ffff000090723ec4  size=288  FUN_ffff000090723ec4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090723ec4(void)

{
  long lVar1;
  byte bVar2;
  byte bVar3;
  byte bVar4;
  byte bVar5;
  long lVar6;
  ulong uVar7;
  undefined1 *puVar8;
  
  uVar7 = (ulong)_DAT_ffff000090860920;
  if ((int)_DAT_ffff000090860920 < 1) {
    _DAT_ffff000090861468 = 0;
    _DAT_ffff00009086146c = 0;
  }
  else {
    _DAT_ffff000090861468 = 0;
    _DAT_ffff00009086146c = 0;
    puVar8 = &DAT_ffff0000908607b8;
    lVar6 = 0;
    do {
      bVar2 = puVar8[0x14];
      bVar3 = puVar8[0x15];
      _DAT_ffff000090861468 = _DAT_ffff000090861468 + 8;
      bVar4 = puVar8[0x16];
      lVar1 = lVar6 + 0xc0;
      bVar5 = puVar8[0x17];
      *(uint *)(&DAT_ffff000090860938 + lVar6) = (uint)bVar2;
      *(uint *)(&DAT_ffff000090860950 + lVar6) = (uint)bVar3;
      *(uint *)(lVar6 + -0xffff6f79f668) = (uint)bVar2;
      *(uint *)(lVar6 + -0xffff6f79f650) = (uint)bVar3;
      *(undefined1 **)(lVar6 + -0xffff6f79f6d8) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f6c0) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f6a8) = puVar8;
      _DAT_ffff00009086146c =
           _DAT_ffff00009086146c + ((uint)bVar3 + (uint)bVar2 + (uint)bVar4 + (uint)bVar5) * 2;
      *(undefined1 **)(lVar6 + -0xffff6f79f690) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f678) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f660) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f648) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f630) = puVar8;
      puVar8 = puVar8 + 0x18;
      *(undefined1 *)(lVar6 + -0xffff6f79f6cc) = 0;
      *(undefined4 *)(lVar6 + -0xffff6f79f6d0) = 0;
      *(undefined1 *)(lVar6 + -0xffff6f79f6b4) = 0;
      *(undefined4 *)(lVar6 + -0xffff6f79f6b8) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f6a0) = 2;
      *(undefined1 *)(lVar6 + -0xffff6f79f69c) = 0;
      *(uint *)(lVar6 + -0xffff6f79f698) = (uint)bVar4;
      *(undefined1 *)(lVar6 + -0xffff6f79f684) = 0;
      *(uint *)(lVar6 + -0xffff6f79f680) = (uint)bVar5;
      *(undefined4 *)(lVar6 + -0xffff6f79f688) = 3;
      *(undefined1 *)(lVar6 + -0xffff6f79f66c) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f670) = 0;
      *(undefined1 *)(lVar6 + -0xffff6f79f654) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f658) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f640) = 2;
      *(undefined1 *)(lVar6 + -0xffff6f79f63c) = 1;
      *(uint *)(lVar6 + -0xffff6f79f638) = (uint)bVar4;
      *(undefined1 *)(lVar6 + -0xffff6f79f624) = 1;
      *(uint *)(lVar6 + -0xffff6f79f620) = (uint)bVar5;
      *(undefined4 *)(lVar6 + -0xffff6f79f628) = 3;
      lVar6 = lVar1;
    } while (uVar7 * 0xc0 - lVar1 != 0);
  }
  return;
}


