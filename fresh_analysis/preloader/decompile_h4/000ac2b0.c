// File offset entry: 0xac2b0

undefined4 FUN_020ac1b0(ulong param_1)

{
  bool bVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined4 uVar4;
  
  bVar1 = (param_1 & 1) == 0;
  uVar2 = 0x20b617a;
  if (bVar1) {
    uVar2 = 0x20b51c3;
  }
  uVar4 = 0;
  if (bVar1) {
    uVar4 = 0x6b32970a;
  }
  FUN_0208ada0(uVar2);
  uVar3 = 0x1001;
  uVar2 = 0x20ccdc4;
  do {
    uRam000000001326f010 = uVar4;
    if ((uRam000000001326f000 >> 1 & 1) == 0) {
      uVar4 = 0;
      goto LAB_020ac23c;
    }
    uVar3 = uVar3 - 1;
  } while (1 < uVar3);
  FUN_0208ada0(0x20ba4bf,2,0);
  uVar4 = 0x1000000;
  uVar2 = 0x20b9367;
LAB_020ac23c:
  FUN_0208ada0(uVar2);
  return uVar4;
}


