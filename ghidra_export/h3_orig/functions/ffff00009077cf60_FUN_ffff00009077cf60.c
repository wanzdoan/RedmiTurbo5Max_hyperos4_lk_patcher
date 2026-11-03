// ffff00009077cf60  size=244  FUN_ffff00009077cf60

undefined8 FUN_ffff00009077cf60(long *param_1,long *param_2,uint param_3)

{
  long *plVar1;
  undefined8 uVar2;
  ulong uVar3;
  long *plVar4;
  long lVar5;
  
  if (param_2 == (long *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04(s_lib_dpc_dpc_c_ffff0000907ed05a,0x2e,&DAT_ffff0000907deb79);
  }
  if (param_2[2] != 0) {
    uVar2 = daif;
    plVar1 = (long *)&DAT_ffff00009087cee0;
    if (param_1 != (long *)0x0) {
      plVar1 = param_1;
    }
    if (((uint)uVar2 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 | 0x80;
    }
    plVar4 = (long *)param_2[3];
    plVar1[8] = 1;
    if ((plVar4 != (long *)0x0) && (plVar4 != plVar1)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_lib_dpc_dpc_c_ffff0000907ed05a,0x36,s__work_>q_____work_>q____q__ffff0000907db6f4
                );
    }
    if ((*param_2 == 0) && (param_2[1] == 0)) {
      lVar5 = *plVar1;
      param_2[1] = (long)plVar1;
      param_2[3] = (long)plVar1;
      *param_2 = lVar5;
      lVar5 = *plVar1;
      *plVar1 = (long)param_2;
      *(long **)(lVar5 + 8) = param_2;
      plVar1[8] = 0;
    }
    else {
      plVar1[8] = 0;
    }
    if (((uint)uVar2 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 & 0xffffffffffffff7f;
    }
    FUN_ffff0000907174e0(plVar1 + 2,param_3 & 1);
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04(s_lib_dpc_dpc_c_ffff0000907ed05a,0x2f,s_work_>cb_ffff0000907eebb4);
}


