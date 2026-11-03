// File offset entry: 0x633ac

uint FUN_020632ac(void)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  uVar2 = uRam000000000211bda8 >> 0x14;
  uVar3 = uRam000000000211bda8 >> 0xc;
  uVar1 = uRam000000000211bda8 & 0xfff;
  FUN_0208ada0(0x20ce5a8,uRam000000000211bda8);
  FUN_0208ada0(0x20c714a,uVar2 & 0x7ff);
  FUN_0208ada0(0x20cd30d,uVar3 & 0xff);
  FUN_0208ada0(0x20c5ec9,uVar1);
  return uRam000000000211bda8;
}


