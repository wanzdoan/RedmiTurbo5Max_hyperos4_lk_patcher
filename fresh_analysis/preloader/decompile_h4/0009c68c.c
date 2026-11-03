// File offset entry: 0x9c68c

/* WARNING: Possible PIC construction at 0x020a0d3c: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a0da4: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a0d40) */
/* WARNING: Removing unreachable block (ram,0x020a0d88) */
/* WARNING: Removing unreachable block (ram,0x020a0d70) */
/* WARNING: Removing unreachable block (ram,0x020a0da8) */
/* WARNING: Removing unreachable block (ram,0x020a0db8) */
/* WARNING: Removing unreachable block (ram,0x020a0dec) */
/* WARNING: Removing unreachable block (ram,0x020a0dd4) */

ulong FUN_0209c58c(ulong param_1,uint param_2,long param_3,int param_4)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  undefined1 auStack_110 [72];
  undefined1 auStack_c8 [72];
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  undefined8 uStack_38;
  
  if (param_4 - 1U < 2) {
    if ((param_1 != 0) && (param_3 != 0)) {
      if (param_2 != 0) {
        func_0x020abf40(0x10000000,1);
        func_0x020abea8(0x10000000,2);
        func_0x020abfa4(0x18000000,1);
        uVar2 = func_0x020aaae8(0x18005000,param_1,param_2,param_3);
        func_0x020abea8(0x10000000,0);
        return uVar2 & 0xffffffff;
      }
      return 1;
    }
    return 0xffffffff;
  }
  if (param_4 != 0) {
    return 0x10101b;
  }
  if ((param_1 != 0) && (param_3 != 0)) {
    uVar2 = (ulong)param_2;
    if ((cRam0000000002122248 == '\x01') && (iVar1 = func_0x020a0710(), iVar1 == 5)) {
      uStack_38 = *puRam0000000002103280;
      uStack_50 = 0;
      uStack_48 = 0;
      uStack_60 = 0;
      uStack_58 = 0;
      uStack_70 = 0;
      uStack_68 = 0;
      uStack_80 = 0;
      uStack_78 = 0;
      FUN_02091664(auStack_c8,0,0x48);
      FUN_02091664(auStack_110,0,0x48);
      func_0x020a0aa0();
      uVar3 = param_1 & 0xffffffffffffffc0;
      do {
        DC_CIVAC(uVar3);
        uVar3 = uVar3 + 0x40;
      } while (uVar3 < param_1 + uVar2);
      DataSynchronizationBarrier(3,3,0);
      return param_1;
    }
    if (param_1 == 0) {
      uVar2 = 2;
    }
    else if (uVar2 == 0) {
      uVar2 = 1;
    }
    else {
      func_0x020abf40(0x10000000,1);
      func_0x020abea8(0x10000000,2);
      func_0x020abfa4(0x18000000,1);
      uVar2 = func_0x020aa860(0x18005000,param_1,uVar2,param_3);
      uVar2 = uVar2 & 0xffffffff;
      func_0x020abea8(0x10000000,0);
    }
    return uVar2;
  }
  return 0xffffffff;
}


