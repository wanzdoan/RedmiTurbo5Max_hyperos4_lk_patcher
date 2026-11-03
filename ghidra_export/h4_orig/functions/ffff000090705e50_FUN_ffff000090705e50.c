// ffff000090705e50  size=716  FUN_ffff000090705e50

void FUN_ffff000090705e50(uint *param_1,uint *param_2,uint param_3)

{
  uint uVar1;
  ulong uVar2;
  uint uVar3;
  ulong uVar4;
  long lVar5;
  ulong uVar6;
  uint local_94 [9];
  ulong local_70;
  undefined8 local_68;
  uint local_60;
  undefined8 local_58;
  
  local_58 = 0;
  local_60 = 0;
  local_70 = 0;
  local_68 = 0;
  local_94[7] = 0;
  local_94[3] = 0;
  local_94[4] = 0;
  local_94[5] = 0;
  local_94[6] = 0;
  if (1 < param_3) {
    FUN_ffff00009077e11c
              (s__s__d__bat_idx__u__BUG__ffff0000907f9207,s_mt6379_do_ptim_gauge_ffff0000907cf033,
               0x16f,param_3);
    return;
  }
  lVar5 = 0;
  uVar6 = 0;
  do {
    FUN_ffff00009070611c((long)&local_58 + 4,&local_58,param_3);
    uVar3 = local_58._4_4_;
    if (uVar6 == 0) {
      local_70 = CONCAT44(local_70._4_4_,local_58._4_4_);
LAB_ffff000090706028:
      uVar3 = (uint)local_58;
LAB_ffff00009070602c:
      uVar4 = 0;
    }
    else {
      uVar2 = uVar6 - 1;
      uVar1 = *(uint *)((long)&local_70 + (uVar2 & 0xffffffff) * 4);
      uVar4 = uVar6;
      if (local_58._4_4_ < uVar1) {
        *(uint *)((long)&local_70 + lVar5) = uVar1;
        if (uVar6 < 2) {
LAB_ffff000090705fa4:
          uVar4 = 0;
        }
        else {
          uVar1 = *(uint *)((long)&local_70 + (uVar6 - 2 & 0xffffffff) * 4);
          uVar4 = uVar2;
          if (uVar3 < uVar1) {
            *(uint *)((long)local_94 + lVar5 + 0x20) = uVar1;
            if (uVar6 < 3) goto LAB_ffff000090705fa4;
            uVar1 = *(uint *)((long)&local_70 + (uVar6 - 3 & 0xffffffff) * 4);
            uVar4 = uVar6 - 2;
            if (uVar3 < uVar1) {
              *(uint *)((long)local_94 + lVar5 + 0x1c) = uVar1;
              if (uVar6 < 4) goto LAB_ffff000090705fa4;
              uVar1 = *(uint *)((long)&local_70 + (ulong)((int)uVar6 - 4) * 4);
              uVar4 = uVar6 - 3;
              if (uVar3 < uVar1) {
                uVar4 = 0;
                *(uint *)((long)local_94 + lVar5 + 0x18) = uVar1;
              }
            }
          }
        }
      }
      *(uint *)((long)&local_70 +
               (-(uVar4 >> 0x1f & 1) & 0xfffffffc00000000 | (uVar4 & 0xffffffff) << 2)) = uVar3;
      if (uVar6 == 0) goto LAB_ffff000090706028;
      uVar3 = (uint)local_58;
      uVar4 = uVar6;
      if ((uint)local_58 < local_94[(uVar2 & 0xffffffff) + 3]) {
        *(uint *)((long)local_94 + lVar5 + 0xc) = local_94[(uVar2 & 0xffffffff) + 3];
        if (1 < uVar6) {
          uVar4 = uVar2;
          if (uVar3 < local_94[(uVar6 - 2 & 0xffffffff) + 3]) {
            *(uint *)((long)local_94 + lVar5 + 8) = local_94[(uVar6 - 2 & 0xffffffff) + 3];
            if (uVar6 < 3) goto LAB_ffff00009070602c;
            uVar4 = uVar6 - 2;
            if (uVar3 < local_94[(uVar6 - 3 & 0xffffffff) + 3]) {
              *(uint *)((long)local_94 + lVar5 + 4) = local_94[(uVar6 - 3 & 0xffffffff) + 3];
              if (uVar6 < 4) goto LAB_ffff00009070602c;
              uVar4 = uVar6 - 3;
              if (uVar3 < local_94[(ulong)((int)uVar6 - 4) + 3]) {
                *(uint *)((long)local_94 + lVar5) = local_94[(ulong)((int)uVar6 - 4) + 3];
                uVar4 = 0;
              }
            }
          }
          goto LAB_ffff000090705eec;
        }
        goto LAB_ffff00009070602c;
      }
    }
LAB_ffff000090705eec:
    uVar6 = uVar6 + 1;
    lVar5 = lVar5 + 4;
    *(uint *)((long)local_94 +
             (-(uVar4 >> 0x1f & 1) & 0xfffffffc00000000 | (uVar4 & 0xffffffff) << 2) + 0xc) = uVar3;
    if (uVar6 == 5) {
      *param_1 = (uint)local_68;
      *param_2 = local_94[5];
      FUN_ffff00009077e11c
                (s__s__d__BAT_d_imp_vbat__d__d__d___ffff0000907f7957,
                 s_mt6379_do_ptim_gauge_ffff0000907cf033,0x191,param_3 + 1,(ulong)*param_1 / 10,
                 (local_70 & 0xffffffff) / 10,local_70 / 0xa00000000,(local_68 & 0xffffffff) / 10,
                 local_68._4_4_ / 10,local_60 / 10,local_94[5] / 10,local_94[3] / 10,
                 local_94[4] / 10,local_94[5] / 10,local_94[6] / 10,local_94[7] / 10);
      return;
    }
  } while( true );
}


