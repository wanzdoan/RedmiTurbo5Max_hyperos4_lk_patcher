// File offset entry: 0x4c10

ulong FUN_02004b10(undefined8 param_1)

{
  undefined1 in_ZR;
  bool bVar1;
  uint uVar2;
  ulong uVar3;
  long lVar4;
  ushort *puVar5;
  undefined8 uVar6;
  byte abStack_48a [2];
  undefined8 uStack_488;
  undefined8 uStack_480;
  undefined8 uStack_478;
  undefined8 uStack_470;
  long lStack_468;
  undefined1 *puStack_460;
  undefined8 uStack_458;
  undefined1 auStack_428 [1024];
  long lStack_28;
  
  lStack_28 = *plRam0000000002103280;
  FUN_02091664(auStack_428,0,0x400);
  func_0x0205ca98(auStack_428);
  func_0x0205cae8(0xffffffff,param_1,0x1c,auStack_428);
  uVar2 = FUN_02004cf8();
  if ((bool)in_ZR) {
    return (ulong)~uVar2;
  }
  FUN_02091090();
  uStack_458 = 0x2004b88;
  lStack_468 = *plRam0000000002103280;
  uStack_478 = 0;
  uStack_470 = 0;
  uStack_488 = 0;
  uStack_480 = 0;
  puStack_460 = &stack0xffffffffffffffe0;
  FUN_020915ac(&uStack_488,0x210e008,0x20);
  FUN_02091664(0x210e008,0,0x20);
  bVar1 = (byte)uStack_480 < uStack_480._4_1_;
  FUN_0208ada0(0x20ccf34,uStack_480 & 0xff,uStack_480._4_1_,uStack_480._1_1_,uStack_480._5_1_);
  uVar6 = *(undefined8 *)((ulong)bVar1 * 8 + 0x20e51d8);
  uVar3 = FUN_02091524(uVar6);
  if (uVar3 < 4) {
    lVar4 = FUN_02091524(uVar6);
  }
  else {
    lVar4 = 2;
  }
  FUN_020915ac(0x210e008,uVar6,lVar4);
  *(undefined1 *)(lVar4 + 0x210e008) = 0;
  uRam000000000210e00c = 0x42414342;
  uRam000000000210e010 = 1;
  bRam000000000210e011 = bRam000000000210e011 & 0xf8 | 2;
  puVar5 = (ushort *)0x210e014;
  for (lVar4 = 10; lVar4 != 0x12; lVar4 = lVar4 + 4) {
    *puVar5 = (*(byte *)((long)&uStack_488 + lVar4) & 1) << 7 |
              (ushort)*(byte *)((long)puVar5 + 1) << 8 |
              abStack_48a[lVar4] & 0xf | (abStack_48a[lVar4 + 1] & 7) << 4;
    puVar5 = puVar5 + 1;
  }
  uRam000000000210e01c = 1;
  uVar3 = FUN_02004b10(0x210e008);
  uRam000000000210e024 = (undefined4)uVar3;
  if (*plRam0000000002103280 == lStack_468) {
    return uVar3;
  }
  uVar3 = FUN_02091090();
  return uVar3;
}


