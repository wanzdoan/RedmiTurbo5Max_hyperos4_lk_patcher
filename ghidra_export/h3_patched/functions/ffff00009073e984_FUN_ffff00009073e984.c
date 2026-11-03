// ffff00009073e984  size=740  FUN_ffff00009073e984

undefined8
FUN_ffff00009073e984(long param_1,int *param_2,uint *param_3,uint param_4,undefined8 param_5)

{
  bool bVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  ulong uVar5;
  int iVar6;
  ulong uVar7;
  
  if ((param_1 != 0) && (param_2 != (int *)0x0)) {
    uVar2 = *param_3;
    if ((uVar2 >> 8 & 1) != 0) {
      iVar3 = FUN_ffff00009077f434
                        (param_1 + *param_2,0xa000 - (long)*param_2,
                         s__s_tracker_detects_AR_timeout__ffff0000907e9484,param_5);
      *param_2 = *param_2 + iVar3;
    }
    iVar3 = *param_2;
    if ((uVar2 >> 9 & 1) != 0) {
      iVar3 = FUN_ffff00009077f434
                        (param_1 + iVar3,0xa000 - (long)iVar3,
                         s__s_tracker_detects_AW_timeout__ffff0000907e2e7a,param_5);
      iVar3 = *param_2 + iVar3;
      *param_2 = iVar3;
    }
    iVar4 = FUN_ffff00009077f434
                      (param_1 + iVar3,(long)(0xa000 - iVar3),
                       s__s_tracker_raw_status__0x_x_ffff0000907f9d0b,param_5,uVar2);
    iVar3 = *param_2;
    iVar6 = (int)((long)iVar3 + (long)iVar4);
    *param_2 = iVar6;
    uVar5 = *(ulong *)(param_3 + 0x12);
    uVar7 = uVar5;
    if (uVar5 != 0) {
      do {
        uVar5 = uVar7 >> 1 ^ uVar5;
        bVar1 = 1 < uVar7;
        uVar7 = uVar7 >> 1;
      } while (bVar1);
      if (uVar5 != 0) {
        iVar3 = FUN_ffff00009077f434
                          (param_1 + (long)iVar3 + (long)iVar4,(long)(0xa000 - iVar6),
                           s__s_tracker_timestamp__0x_llx__ffff0000907e62f6,param_5);
        iVar3 = *param_2 + iVar3;
        *param_2 = iVar3;
        if (param_4 != 0) {
          iVar4 = 0;
          uVar5 = 0;
          do {
            uVar7 = uVar5 & 0xfffffffc;
            uVar2 = *(uint *)((long)param_3 + uVar7 + 0x200);
            iVar3 = FUN_ffff00009077f434
                              (param_1 + iVar3,(long)(0xa000 - iVar3),
                               s_read_entry____d__valid___0x_x__n_ffff0000907e1579,iVar4,
                               uVar2 >> 0x1f,uVar2 >> 8 & 1,
                               *(undefined4 *)((long)param_3 + uVar7 + 0x300),
                               CONCAT44(*(undefined4 *)((long)param_3 + uVar7 + 0x600),
                                        *(undefined4 *)((long)param_3 + uVar7 + 0x400)),
                               uVar2 >> 4 & 7,uVar2 & 0xf);
            uVar5 = uVar5 + 4;
            iVar4 = iVar4 + 1;
            iVar3 = *param_2 + iVar3;
            *param_2 = iVar3;
          } while ((ulong)param_4 << 2 != uVar5);
          if (param_4 != 0) {
            iVar4 = 0;
            uVar5 = 0;
            do {
              uVar7 = uVar5 & 0xfffffffc;
              uVar2 = *(uint *)((long)param_3 + uVar7 + 0x800);
              iVar3 = FUN_ffff00009077f434
                                (param_1 + iVar3,(long)(0xa000 - iVar3),
                                 s_write_entry____d__valid___0x_x__n_ffff0000907d1384,iVar4,
                                 uVar2 >> 0x1f,uVar2 >> 8 & 1,
                                 *(undefined4 *)((long)param_3 + uVar7 + 0x900),
                                 CONCAT44(*(undefined4 *)((long)param_3 + uVar7 + 0xc00),
                                          *(undefined4 *)((long)param_3 + uVar7 + 0xa00)),
                                 uVar2 >> 4 & 7,uVar2 & 0xf);
              uVar5 = uVar5 + 4;
              iVar4 = iVar4 + 1;
              iVar3 = *param_2 + iVar3;
              *param_2 = iVar3;
            } while ((ulong)param_4 << 2 != uVar5);
          }
        }
        iVar4 = FUN_ffff00009077f434
                          (param_1 + iVar3,(long)(0xa000 - iVar3),
                           s_write_entry____d__valid___0x_x__d_ffff0000907e94c4,param_4 - 2,
                           param_3[8] >> 6 & 1,param_3[0x3be]);
        iVar3 = *param_2;
        iVar6 = (int)((long)iVar3 + (long)iVar4);
        *param_2 = iVar6;
        iVar3 = FUN_ffff00009077f434
                          (param_1 + (long)iVar3 + (long)iVar4,(long)(0xa000 - iVar6),
                           s_write_entry____d__valid___0x_x__d_ffff0000907e94f1,param_4 - 1,
                           param_3[8] >> 7 & 1,param_3[0x3bf]);
        *param_2 = *param_2 + iVar3;
        return 0;
      }
    }
    FUN_ffff00009077de54(s_Get__s_tracker_timestamp_fail__ffff0000907e94a4,param_5);
  }
  return 0xffffffff;
}


