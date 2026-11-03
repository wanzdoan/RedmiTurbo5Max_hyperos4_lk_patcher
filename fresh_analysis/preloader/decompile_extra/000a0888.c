// File offset entry: 0xa0888

undefined4 FUN_020a0788(void)

{
  uint uVar1;
  undefined4 uVar2;
  uint uVar3;
  undefined4 uVar4;
  
  uVar1 = uRam000000001002d8f0 & 0x80000;
  uVar3 = uRam000000001002d8f0 & 0x40000;
  if ((uRam0000000013260058 & 3) != 0) {
    uVar1 = uRam0000000013260058 & 1;
    uVar3 = uRam0000000013260058 & 2;
  }
  uVar4 = 1;
  if (uVar3 != 0) {
    uVar4 = 2;
  }
  uVar2 = 0;
  if (uVar1 != 0) {
    uVar2 = uVar4;
  }
  return uVar2;
}


