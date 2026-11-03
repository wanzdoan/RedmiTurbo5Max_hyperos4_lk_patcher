// ffff00009073ff04  size=356  FUN_ffff00009073ff04

uint FUN_ffff00009073ff04
               (long *param_1,byte param_2,byte param_3,uint param_4,undefined8 param_5,byte param_6
               )

{
  long lVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  undefined4 local_44;
  
  uVar4 = 0xfffffff8;
  if ((param_3 < 0x11) && (param_6 < 3)) {
    if ((char)param_2 < '`') {
      if ((param_2 & 0xf0) == 0x20) {
        uVar4 = 0x80000000;
      }
      else {
        if ((param_2 & 0xf8) != 0x38) {
          return 0xfffffff8;
        }
        uVar4 = 0xc0000000;
      }
    }
    else {
      uVar4 = 0x40000000;
    }
    FUN_ffff0000907175f4(&LAB_ffff00009081a0d0,0xffffffff);
    do {
    } while ((*(uint *)(*(long *)(&DAT_ffff00009081a010 + (long)(int)param_1[3] * 0x40) +
                       (ulong)*(uint *)((&PTR_LAB_ffff00009081a018)[(long)(int)param_1[3] * 8] +
                                       0x48)) & 0xe) != 0);
    lVar1 = *param_1;
    lVar2 = param_1[1];
    *(uint *)(lVar1 + (ulong)*(uint *)(lVar2 + 0x30)) =
         uVar4 | (uint)param_3 << 0x18 | (uint)(byte)(param_6 - 1) << 0x10 | param_4 & 0xffff;
    do {
    } while ((*(uint *)(*(long *)(&DAT_ffff00009081a010 + (long)(int)param_1[3] * 0x40) +
                       (ulong)*(uint *)((&PTR_LAB_ffff00009081a018)[(long)(int)param_1[3] * 8] +
                                       0x48)) & 0xe) != 0xc);
    iVar3 = *(int *)(lVar1 + (ulong)*(uint *)(lVar2 + 0x48));
    local_44 = *(undefined4 *)(lVar1 + (ulong)*(uint *)(lVar2 + 0x3c));
    FUN_ffff00009077e858(param_5,&local_44,(param_6 - 1 & 3) + 1);
    *(undefined4 *)(*param_1 + (ulong)*(uint *)(param_1[1] + 0x44)) = 1;
    FUN_ffff00009071768c(&LAB_ffff00009081a0d0);
    uVar4 = (iVar3 << 0xd) >> 0x1f & 0xffffffec;
  }
  return uVar4;
}


