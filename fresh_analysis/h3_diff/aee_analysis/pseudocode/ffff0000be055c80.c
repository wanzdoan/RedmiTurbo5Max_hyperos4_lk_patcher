
uint FUN_ffff0000be055c80(long param_1,undefined4 param_2)

{
  uint uVar1;
  undefined8 uVar2;
  long lVar3;
  uint *puVar4;
  int local_34;
  int local_18;
  int local_14;
  
  local_34 = 0;
  uVar2 = FUN_ffff0000be060e5c(s_phandle_ffff0000be0bc639);
  local_18 = 0;
  lVar3 = FUN_ffff0000be0562f0(param_1,param_2,s_phandle_ffff0000be0bc639,uVar2,&local_34,&local_18)
  ;
  if (lVar3 != 0) {
    uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    if ((((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) && ((local_18 + 4U & 7) != 0)) &&
       (uVar1 = (*(uint *)(lVar3 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 4) & 0xff00ff) << 8,
       7 < (uVar1 >> 0x10 | uVar1 << 0x10))) {
      puVar4 = (uint *)(lVar3 + 0x10);
    }
    else {
      puVar4 = (uint *)(lVar3 + 0xc);
    }
    if (local_34 == 4) goto LAB_ffff0000be055d9c;
  }
  uVar2 = FUN_ffff0000be060e5c(s_linux_phandle_ffff0000be0a875b);
  local_14 = 0;
  lVar3 = FUN_ffff0000be0562f0
                    (param_1,param_2,s_linux_phandle_ffff0000be0a875b,uVar2,&local_34,&local_14);
  if (lVar3 == 0) {
    return 0;
  }
  uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
  if ((((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) && ((local_14 + 4U & 7) != 0)) &&
     (uVar1 = (*(uint *)(lVar3 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 4) & 0xff00ff) << 8,
     7 < (uVar1 >> 0x10 | uVar1 << 0x10))) {
    puVar4 = (uint *)(lVar3 + 0x10);
  }
  else {
    puVar4 = (uint *)(lVar3 + 0xc);
  }
  if (local_34 != 4) {
    return 0;
  }
LAB_ffff0000be055d9c:
  uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
  return uVar1 >> 0x10 | uVar1 << 0x10;
}


