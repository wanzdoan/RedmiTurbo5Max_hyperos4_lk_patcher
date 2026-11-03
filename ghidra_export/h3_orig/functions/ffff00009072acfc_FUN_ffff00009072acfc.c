// ffff00009072acfc  size=280  FUN_ffff00009072acfc

void FUN_ffff00009072acfc(uint param_1,int param_2)

{
  int iVar1;
  long lVar2;
  undefined8 local_68;
  int aiStack_60 [12];
  
  if ((int)param_1 < 9) {
    aiStack_60[10] = 0;
    aiStack_60[6] = 0;
    aiStack_60[7] = 0;
    aiStack_60[8] = 0;
    aiStack_60[9] = 0;
    aiStack_60[2] = 0;
    aiStack_60[3] = 0;
    aiStack_60[4] = 0;
    aiStack_60[5] = 0;
    local_68 = 0;
    aiStack_60[0] = 0;
    aiStack_60[1] = 0;
    FUN_ffff00009077eb88(&local_68,0,0x34);
    iVar1 = FUN_ffff00009072ac00(&local_68);
    if (-1 < iVar1) {
      lVar2 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
      if (lVar2 == 0) {
        FUN_ffff00009077de54
                  (s__s__Partition___s__is_not_exist__ffff0000907fcfbd,
                   s_LOG_STORE_LK_ffff0000907d7514,s_expdb_ffff0000907d5730);
      }
      else {
        if ((param_1 < 8) && ((1 << (ulong)(param_1 & 0x1f) & 0x8dU) != 0)) {
          if (param_2 == 0) {
            aiStack_60[param_1] = 0x44495341;
          }
          else {
            aiStack_60[param_1] = 0x454e454e;
          }
        }
        else {
          aiStack_60[(int)param_1] = param_2;
        }
        FUN_ffff00009077be7c(lVar2,&local_68,*(long *)(lVar2 + 0x20) - *(long *)(lVar2 + 0x28),0x34)
        ;
        FUN_ffff00009077bce4(lVar2);
      }
    }
  }
  return;
}


