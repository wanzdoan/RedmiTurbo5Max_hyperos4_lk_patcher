// File offset entry: 0xa6ee8

undefined8 FUN_020a6de8(long param_1,undefined8 param_2,uint param_3,uint *param_4)

{
  byte bVar1;
  uint uVar2;
  undefined8 uVar3;
  uint uVar4;
  long lVar5;
  ulong uVar6;
  byte *pbVar7;
  
  bVar1 = *(byte *)(param_1 + 1);
  if (((char)bVar1 < '\0') && (uVar6 = (ulong)bVar1 & 0x7f, (int)uVar6 != 0)) {
    uVar4 = 0;
    lVar5 = uVar6 + 1;
    pbVar7 = (byte *)(param_1 + 2);
    do {
      uVar2 = (int)uVar6 - 1;
      uVar6 = (ulong)uVar2;
      uVar4 = (uint)*pbVar7 | uVar4 << 8;
      pbVar7 = pbVar7 + 1;
    } while (uVar2 != 0);
  }
  else {
    uVar4 = (uint)bVar1;
    lVar5 = 1;
  }
  uVar4 = uVar4 - 1;
  *param_4 = uVar4;
  if (param_3 < uVar4) {
    uVar3 = 0x101028;
  }
  else {
    func_0x020a0dfc(param_2,(byte *)(param_1 + 1) + lVar5 + 1,uVar4);
    uVar3 = 0;
  }
  return uVar3;
}


