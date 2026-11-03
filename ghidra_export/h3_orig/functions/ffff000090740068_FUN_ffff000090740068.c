// ffff000090740068  size=364  FUN_ffff000090740068

uint FUN_ffff000090740068
               (long *param_1,uint param_2,byte param_3,uint param_4,undefined8 param_5,byte param_6
               )

{
  uint uVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  uint uVar5;
  undefined4 local_54;
  
  uVar5 = 0xfffffff8;
  local_54 = 0;
  if ((param_3 < 0x11) && (param_6 < 3)) {
    if ((param_2 & 0xe0) == 0x40) {
      uVar5 = 0x60000000;
    }
    else if ((param_2 & 0xff) < 0x10) {
      uVar5 = 0xa0000000;
    }
    else if ((param_2 & 0xf8) == 0x30) {
      uVar5 = 0xe0000000;
    }
    else {
      if ((param_2 & 0xff) >> 7 == 0) {
        return 0xfffffff8;
      }
      uVar5 = 0x20000000;
    }
    FUN_ffff0000907175f4(&LAB_ffff00009081a0d0,0xffffffff);
    do {
    } while ((*(uint *)(*(long *)(&DAT_ffff00009081a010 + (long)(int)param_1[3] * 0x40) +
                       (ulong)*(uint *)((&PTR_LAB_ffff00009081a018)[(long)(int)param_1[3] * 8] +
                                       0x48)) & 0xe) != 0);
    uVar1 = *(uint *)(param_1[1] + 0x34);
    FUN_ffff00009077e858(&local_54,param_5,(param_6 - 1 & 3) + 1);
    lVar3 = *param_1;
    *(undefined4 *)(lVar3 + (ulong)uVar1) = local_54;
    lVar4 = param_1[1];
    *(uint *)(lVar3 + (ulong)*(uint *)(lVar4 + 0x30)) =
         uVar5 | (uint)param_3 << 0x18 | (uint)(byte)(param_6 - 1) << 0x10 | param_4 & 0xffff;
    do {
    } while ((*(uint *)(*(long *)(&DAT_ffff00009081a010 + (long)(int)param_1[3] * 0x40) +
                       (ulong)*(uint *)((&PTR_LAB_ffff00009081a018)[(long)(int)param_1[3] * 8] +
                                       0x48)) & 0xe) != 0);
    iVar2 = *(int *)(lVar3 + (ulong)*(uint *)(lVar4 + 0x48));
    FUN_ffff00009071768c(&LAB_ffff00009081a0d0);
    uVar5 = (iVar2 << 0xd) >> 0x1f & 0xffffffec;
  }
  return uVar5;
}


