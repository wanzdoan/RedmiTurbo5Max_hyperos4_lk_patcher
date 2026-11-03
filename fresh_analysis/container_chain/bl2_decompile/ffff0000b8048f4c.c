
undefined4 FUN_ffff0000b8048f4c(uint *param_1)

{
  uint uVar1;
  uint uVar2;
  undefined4 *puVar3;
  undefined4 uVar4;
  
  uVar1 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (uVar1 == 0x2ff20112) {
    uVar1 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
    uVar2 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
    if ((uVar1 >> 0x10 | uVar1 << 0x10) == (uVar2 >> 0x10 | uVar2 << 0x10)) {
      puVar3 = (undefined4 *)FUN_ffff0000b8048ec0(param_1,4);
      if (puVar3 == (undefined4 *)0x0) {
        uVar4 = 0xfffffffd;
      }
      else {
        uVar4 = 0;
        *puVar3 = 0x2000000;
      }
    }
    else {
      uVar4 = 0xfffffff9;
    }
  }
  else {
    uVar4 = 0xfffffff9;
    if (uVar1 != 0xd00dfeed) {
      uVar4 = 0xfffffff7;
    }
  }
  return uVar4;
}


