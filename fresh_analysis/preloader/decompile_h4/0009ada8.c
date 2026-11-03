// File offset entry: 0x9ada8

int FUN_0209aca8(uint param_1)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  if (param_1 == 0) {
    uVar3 = 0x20ccd01;
  }
  else if (uRam0000000002121ff8 < param_1) {
    uVar3 = 0x20c042c;
  }
  else {
    if (iRam0000000002121ff4 + param_1 <= uRam0000000002121ff8) {
      uVar1 = param_1 + 3 & 0xfffffffc;
      iVar2 = func_0x0208cbdc();
      iVar2 = iVar2 + iRam0000000002121ff4;
      iRam0000000002121ff4 = iRam0000000002121ff4 + uVar1;
      FUN_02091664(iVar2,0,uVar1);
      return iVar2;
    }
    uVar3 = 0x20c8915;
  }
  FUN_0208ada0(uVar3,0x20c4649);
  return 0;
}


