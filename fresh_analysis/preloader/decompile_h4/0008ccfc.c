// File offset entry: 0x8ccfc

uint FUN_0208cbfc(undefined8 param_1,undefined8 param_2,undefined8 param_3,uint param_4)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  ulong uVar4;
  
  uVar4 = FUN_0207e78c(param_1,param_3,param_2);
  if (uVar4 == param_4) {
    return 0;
  }
  uVar2 = uRam000000000211bda8 >> 0x14;
  uVar3 = uRam000000000211bda8 >> 0xc;
  uVar1 = uRam000000000211bda8 & 0xfff;
  FUN_0208ada0(0x20ce5a8,uRam000000000211bda8);
  FUN_0208ada0(0x20c714a,uVar2 & 0x7ff);
  FUN_0208ada0(0x20cd30d,uVar3 & 0xff);
  FUN_0208ada0(0x20c5ec9,uVar1);
  return uRam000000000211bda8;
}


