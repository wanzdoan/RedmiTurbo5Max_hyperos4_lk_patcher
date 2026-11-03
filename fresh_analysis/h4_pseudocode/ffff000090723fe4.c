
void FUN_ffff000090723fe4(int param_1,uint param_2)

{
  bool bVar1;
  bool bVar2;
  bool bVar3;
  int iVar4;
  ulong uVar5;
  
  if (param_2 == 0) {
    iVar4 = 0;
    bVar1 = false;
    uVar5 = 1;
    bVar2 = true;
LAB_ffff000090724088:
    do {
      do {
        if ((&DAT_ffff000090861478)[uVar5] != param_1) {
          DataSynchronizationBarrier(3,3,0);
          iVar4 = iVar4 + 1;
          if (iVar4 == 0x1fffffff) {
            FUN_ffff00009077e11c
                      (s__thread_0__SYNC_NO_ACK_TIMEOUT__t_ffff0000907f28af,uVar5 & 0xffffffff);
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,0,DAT_ffff000090861478,
                       param_1);
            bVar1 = true;
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,1,DAT_ffff00009086147c,
                       param_1);
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,2,DAT_ffff000090861480,
                       param_1);
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,3,DAT_ffff000090861484,
                       param_1);
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,4,DAT_ffff000090861488,
                       param_1);
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,5,DAT_ffff00009086148c,
                       param_1);
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,6,DAT_ffff000090861490,
                       param_1);
            FUN_ffff00009077e11c
                      (s__thread_0__see__thread__d_sync_f_ffff0000907e7756,7,DAT_ffff000090861494,
                       param_1);
            bVar3 = uVar5 == 7;
            iVar4 = 0x1fffffff;
            uVar5 = uVar5 + 1;
            bVar2 = false;
            if (bVar3) goto LAB_ffff0000907241b0;
            goto LAB_ffff000090724088;
          }
          bVar2 = false;
        }
        uVar5 = uVar5 + 1;
      } while (uVar5 != 8);
      if (bVar1) {
LAB_ffff0000907241b0:
        FUN_ffff00009077e11c(s__thread_0_sync_go_to_while1_ffff0000907ec2e8);
        do {
                    /* WARNING: Do nothing block with infinite loop */
        } while( true );
      }
      bVar1 = false;
      uVar5 = 1;
      bVar3 = !bVar2;
      bVar2 = true;
    } while (bVar3);
    InstructionSynchronizationBarrier();
    DataSynchronizationBarrier(3,3,0);
    DAT_ffff000090861478 = param_1;
  }
  else {
    (&DAT_ffff000090861478)[param_2] = param_1;
    InstructionSynchronizationBarrier();
    DataSynchronizationBarrier(3,3,0);
    while (DAT_ffff000090861478 != param_1) {
      InstructionSynchronizationBarrier();
      DataSynchronizationBarrier(3,3,0);
    }
  }
  return;
}


