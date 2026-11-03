
undefined4 FUN_ffff0000be056e1c(uint *param_1,undefined8 param_2)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  undefined4 *puVar4;
  undefined4 uVar5;
  
  uVar1 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (uVar1 == 0x2ff20112) {
    uVar1 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
    uVar2 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
    if ((uVar1 >> 0x10 | uVar1 << 0x10) == (uVar2 >> 0x10 | uVar2 << 0x10)) {
      iVar3 = FUN_ffff0000be060e5c(param_2);
      puVar4 = (undefined4 *)
               FUN_ffff0000be056ee8(param_1,((long)(iVar3 + 1) + 3U & 0xfffffffffffffffc) + 4);
      if (puVar4 == (undefined4 *)0x0) {
        uVar5 = 0xfffffffd;
      }
      else {
        *puVar4 = 0x1000000;
        FUN_ffff0000be0607b0(puVar4 + 1,param_2,(long)(iVar3 + 1));
        uVar5 = 0;
      }
    }
    else {
      uVar5 = 0xfffffff9;
    }
  }
  else {
    uVar5 = 0xfffffff9;
    if (uVar1 != 0xd00dfeed) {
      uVar5 = 0xfffffff7;
    }
  }
  return uVar5;
}


