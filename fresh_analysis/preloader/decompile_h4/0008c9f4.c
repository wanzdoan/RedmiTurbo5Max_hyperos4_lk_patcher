// File offset entry: 0x8c9f4

uint FUN_0208c8f4(int *param_1)

{
  int iVar1;
  uint uVar2;
  long extraout_x8;
  long extraout_x8_00;
  long extraout_x8_01;
  long extraout_x8_02;
  undefined8 uVar3;
  int iStack_70;
  int iStack_6c;
  long lStack_68;
  undefined1 *puStack_60;
  code *pcStack_58;
  int iStack_30;
  int iStack_2c;
  long lStack_28;
  
  lStack_28 = *plRam0000000002103280;
  iStack_30 = 4;
  iStack_2c = 4;
  iVar1 = func_0x0209addc(&iStack_2c);
  if (iVar1 == 0) {
    iVar1 = func_0x0200647c(&iStack_30);
    if (iVar1 != 0) {
      FUN_0208ada0(0x20b0d2e);
      uVar2 = 0x103006;
      goto LAB_0208c99c;
    }
    if (iStack_2c != iStack_30) {
      FUN_0208ada0(0x20c6667);
      uVar2 = 0x103007;
      goto LAB_0208c99c;
    }
    uVar2 = 0;
    iVar1 = iStack_2c;
  }
  else {
    uVar2 = 0x103005;
    FUN_0208ada0(0x20ba162);
LAB_0208c99c:
    iVar1 = 4;
  }
  *param_1 = iVar1;
  if (lRam0000000002100880 == lStack_28) {
    return uVar2;
  }
  FUN_02091090();
  pcStack_58 = FUN_0208c9d8;
  lStack_68 = *plRam0000000002103280;
  iStack_70 = 0;
  iStack_6c = 0;
  puStack_60 = &stack0xffffffffffffffe0;
  iVar1 = FUN_020a4a04(&iStack_6c);
  if (iVar1 != 0) {
    iStack_6c = 1;
  }
  iVar1 = FUN_0208c8f4(&iStack_70);
  if ((iVar1 == 0) && (1 < iStack_70 - 1U)) {
    uVar3 = 0x20c2309;
    if (iStack_70 - 3U < 2) goto LAB_0208ca38;
  }
  else {
    uVar3 = 0x20d1270;
  }
  iStack_70 = 4;
LAB_0208ca38:
  iVar1 = iStack_70;
  FUN_0208ada0(0x20bef72,iStack_6c);
  FUN_0208ada0(uVar3,iVar1);
  if ((iStack_6c == 0) && (iVar1 == 3)) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8 + 0x29);
  }
  else if ((iStack_6c == 0) && (iVar1 != 3)) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8_00 + 0x28);
  }
  else if (iStack_6c == 1 && iVar1 == 3) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8_01 + 0x2b);
  }
  else if (iStack_6c == 1 && iVar1 != 3) {
    func_0x0208cb58();
    uVar2 = (uint)*(byte *)(extraout_x8_02 + 0x2a);
  }
  else {
    uVar2 = 0;
  }
  if (lRam0000000002100880 == lStack_68) {
    return uVar2;
  }
  FUN_02091090(uVar2);
  uVar2 = FUN_0208c9d8();
  return uVar2 >> 1 & 1;
}


