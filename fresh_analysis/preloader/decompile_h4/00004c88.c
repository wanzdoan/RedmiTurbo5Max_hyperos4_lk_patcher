// File offset entry: 0x4c88

void FUN_02004b88(void)

{
  bool bVar1;
  ulong uVar2;
  long lVar3;
  ushort *puVar4;
  undefined8 uVar5;
  byte abStack_5a [2];
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  undefined8 uStack_40;
  long lStack_38;
  
  lStack_38 = *plRam0000000002103280;
  uStack_48 = 0;
  uStack_40 = 0;
  uStack_58 = 0;
  uStack_50 = 0;
  FUN_020915ac(&uStack_58,0x210e008,0x20);
  FUN_02091664(0x210e008,0,0x20);
  bVar1 = (byte)uStack_50 < uStack_50._4_1_;
  FUN_0208ada0(0x20ccf34,uStack_50 & 0xff,uStack_50._4_1_,uStack_50._1_1_,uStack_50._5_1_);
  uVar5 = *(undefined8 *)((ulong)bVar1 * 8 + 0x20e51d8);
  uVar2 = FUN_02091524(uVar5);
  if (uVar2 < 4) {
    lVar3 = FUN_02091524(uVar5);
  }
  else {
    lVar3 = 2;
  }
  FUN_020915ac(0x210e008,uVar5,lVar3);
  *(undefined1 *)(lVar3 + 0x210e008) = 0;
  uRam000000000210e00c = 0x42414342;
  uRam000000000210e010 = 1;
  bRam000000000210e011 = bRam000000000210e011 & 0xf8 | 2;
  puVar4 = (ushort *)0x210e014;
  for (lVar3 = 10; lVar3 != 0x12; lVar3 = lVar3 + 4) {
    *puVar4 = (*(byte *)((long)&uStack_58 + lVar3) & 1) << 7 |
              (ushort)*(byte *)((long)puVar4 + 1) << 8 |
              abStack_5a[lVar3] & 0xf | (abStack_5a[lVar3 + 1] & 7) << 4;
    puVar4 = puVar4 + 1;
  }
  uRam000000000210e01c = 1;
  uRam000000000210e024 = FUN_02004b10(0x210e008);
  if (*plRam0000000002103280 == lStack_38) {
    return;
  }
  FUN_02091090();
  return;
}


