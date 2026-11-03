
uint FUN_ffff0000b804779c(long param_1,undefined4 param_2)

{
  uint uVar1;
  undefined8 uVar2;
  long lVar3;
  uint *puVar4;
  int local_34;
  int local_18;
  int local_14;
  
  local_34 = 0;
  uVar2 = FUN_ffff0000b804d3cc(s_phandle_ffff0000b8099387);
  local_18 = 0;
  lVar3 = FUN_ffff0000b8047e0c(param_1,param_2,s_phandle_ffff0000b8099387,uVar2,&local_34,&local_18)
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
    if (local_34 == 4) goto LAB_ffff0000b80478b8;
  }
  uVar2 = FUN_ffff0000b804d3cc(s_linux_phandle_ffff0000b808953a);
  local_14 = 0;
  lVar3 = FUN_ffff0000b8047e0c
                    (param_1,param_2,s_linux_phandle_ffff0000b808953a,uVar2,&local_34,&local_14);
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
LAB_ffff0000b80478b8:
  uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
  return uVar1 >> 0x10 | uVar1 << 0x10;
}


