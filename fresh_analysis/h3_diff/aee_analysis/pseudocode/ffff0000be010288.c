
ulong FUN_ffff0000be010288(ulong param_1)

{
  undefined8 uVar1;
  long lVar2;
  uint uVar3;
  ulong uVar4;
  char *pcVar5;
  
  uVar1 = FUN_ffff0000be00ca18();
  lVar2 = FUN_ffff0000be00cae8();
  if (((param_1 ^ 0xffffffffffffffff) & 0xff000000000000) != 0) {
    return 0;
  }
  if (lVar2 == 0x4000) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be0fd128,0,0x4000);
    lVar2 = FUN_ffff0000be00cbbc(uVar1,0x4000,&DAT_ffff0000be0fd128);
    if (lVar2 == 0x4000) {
      uVar4 = *(ulong *)(&DAT_ffff0000be0fd128 + (param_1 >> 0x24 & 0x7ff) * 8);
      if ((uVar4 & 3) != 3) {
        if ((uVar4 & 3) != 1) {
          return 0;
        }
        return uVar4 & 0xfff000000000 | param_1 & 0xfffffffff;
      }
      FUN_ffff0000be060ae0(&DAT_ffff0000be0fd128,0,0x4000);
      lVar2 = FUN_ffff0000be00cbbc(uVar4 & 0xffffffffc000,0x4000,&DAT_ffff0000be0fd128);
      if (lVar2 == 0x4000) {
        uVar4 = *(ulong *)(&DAT_ffff0000be0fd128 + (param_1 >> 0x19 & 0x7ff) * 8);
        if ((uVar4 & 3) != 3) {
          if ((uVar4 & 3) != 1) {
            return 0;
          }
          return uVar4 & 0xfffffe000000 | param_1 & 0x1ffffff;
        }
        FUN_ffff0000be060ae0(&DAT_ffff0000be0fd128,0,0x4000);
        lVar2 = FUN_ffff0000be00cbbc(uVar4 & 0xffffffffc000,0x4000,&DAT_ffff0000be0fd128);
        if (lVar2 == 0x4000) {
          uVar3 = (uint)*(ulong *)(&DAT_ffff0000be0fd128 + (param_1 >> 0xe & 0x7ff) * 8);
          uVar4 = *(ulong *)(&DAT_ffff0000be0fd128 + (param_1 >> 0xe & 0x7ff) * 8) & 0xffffffffc000
                  | param_1 & 0x3fff;
LAB_ffff0000be010500:
          if ((~uVar3 & 3) != 0) {
            return 0;
          }
          return uVar4;
        }
        pcVar5 = s_kedump_read_16kps_l3_ptable_fail_ffff0000be0ab2b4;
      }
      else {
        pcVar5 = s_kedump_read_16kps_l2_ptable_fail_ffff0000be0b2b97;
      }
    }
    else {
      pcVar5 = s_kedump__read_16kps_l1_ptable_fai_ffff0000be0c0436;
    }
  }
  else {
    FUN_ffff0000be060ae0(&DAT_ffff0000be101128,0,0x1000);
    lVar2 = FUN_ffff0000be00cbbc(uVar1,0x1000,&DAT_ffff0000be101128);
    if (lVar2 == 0x1000) {
      uVar4 = *(ulong *)(&DAT_ffff0000be101128 + (param_1 >> 0x1e & 0x1ff) * 8);
      if ((uVar4 & 3) != 3) {
        if ((uVar4 & 3) != 1) {
          return 0;
        }
        return uVar4 & 0xffffc0000000 | param_1 & 0x3fffffff;
      }
      FUN_ffff0000be060ae0(&DAT_ffff0000be101128,0,0x1000);
      lVar2 = FUN_ffff0000be00cbbc(uVar4 & 0xfffffffff000,0x1000,&DAT_ffff0000be101128);
      if (lVar2 == 0x1000) {
        uVar4 = *(ulong *)(&DAT_ffff0000be101128 + (param_1 >> 0x15 & 0x1ff) * 8);
        if ((uVar4 & 3) != 3) {
          if ((uVar4 & 3) != 1) {
            return 0;
          }
          return uVar4 & 0xffffffe00000 | param_1 & 0x1fffff;
        }
        FUN_ffff0000be060ae0(&DAT_ffff0000be101128,0,0x1000);
        lVar2 = FUN_ffff0000be00cbbc(uVar4 & 0xfffffffff000,0x1000,&DAT_ffff0000be101128);
        if (lVar2 == 0x1000) {
          uVar3 = (uint)*(ulong *)(&DAT_ffff0000be101128 + (param_1 >> 0xc & 0x1ff) * 8);
          uVar4 = *(ulong *)(&DAT_ffff0000be101128 + (param_1 >> 0xc & 0x1ff) * 8) & 0xfffffffff000
                  | param_1 & 0xfff;
          goto LAB_ffff0000be010500;
        }
        pcVar5 = s_kedump_read_l3_ptable_fail_ffff0000be0ad2ee;
      }
      else {
        pcVar5 = s_kedump__read_l2_ptable_fail_ffff0000be0bab23;
      }
    }
    else {
      pcVar5 = s_kedump__read_l1_ptable_fail_ffff0000be0b60b2;
    }
  }
  FUN_ffff0000be063b40(pcVar5);
  FUN_ffff0000be05fd98(pcVar5);
  return 0;
}


