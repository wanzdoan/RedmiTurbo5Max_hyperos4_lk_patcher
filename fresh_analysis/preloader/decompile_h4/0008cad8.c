// File offset entry: 0x8cad8

uint FUN_0208c9d8(void)

{
  int iVar1;
  uint uVar2;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  long extraout_x8_02;
  undefined8 uVar3;
  int iStack_40;
  int iStack_3c;
  long lStack_38;
  
  lStack_38 = *plRam0000000002103280;
  iStack_40 = 0;
  iStack_3c = 0;
  iVar1 = FUN_020a4a04(&iStack_3c);
  if (iVar1 != 0) {
    iStack_3c = 1;
  }
  iVar1 = func_0x0208c8f4(&iStack_40);
  if ((iVar1 == 0) && (1 < iStack_40 - 1U)) {
    uVar3 = 0x20c2309;
    if (iStack_40 - 3U < 2) goto LAB_0208ca38;
  }
  else {
    uVar3 = 0x20d1270;
  }
  iStack_40 = 4;
LAB_0208ca38:
  iVar1 = iStack_40;
  FUN_0208ada0(0x20bef72,iStack_3c);
  FUN_0208ada0(uVar3,iVar1);
  if ((iStack_3c == 0) && (iVar1 == 3)) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8 + 0x29);
  }
  else if ((iStack_3c == 0) && (iVar1 != 3)) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8_00 + 0x28);
  }
  else if (iStack_3c == 1 && iVar1 == 3) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8_01 + 0x2b);
  }
  else if (iStack_3c == 1 && iVar1 != 3) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8_02 + 0x2a);
  }
  else {
    uVar2 = 0;
  }
  if (lRam0000000002100880 == lStack_38) {
    return uVar2;
  }
  FUN_02091090(uVar2);
  uVar2 = FUN_0208c9d8();
  return uVar2 >> 1 & 1;
}


