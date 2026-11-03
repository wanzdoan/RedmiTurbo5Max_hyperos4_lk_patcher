// File offset entry: 0xac1c4

undefined8 FUN_020ac0c4(undefined4 *param_1,undefined4 param_2)

{
  ulong uVar1;
  
  uVar1 = 0x1001;
  while ((uRam000000001326f000 >> 1 & 1) != 0) {
    uVar1 = uVar1 - 1;
    if (uVar1 < 2) goto LAB_020ac110;
  }
  uVar1 = 0x1001;
  *param_1 = param_2;
  do {
    if ((uRam000000001326f000 >> 1 & 1) == 0) {
      return 0;
    }
    uVar1 = uVar1 - 1;
  } while (1 < uVar1);
LAB_020ac110:
  FUN_0208ada0(0x20ba4bf,2,0);
  return 0x1000000;
}


