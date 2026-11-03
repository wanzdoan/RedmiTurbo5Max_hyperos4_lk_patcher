// ffff0000907949ac  size=292  FUN_ffff0000907949ac

ulong FUN_ffff0000907949ac(undefined8 param_1)

{
  bool bVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  
  lVar4 = FUN_ffff00009077bd60();
  if (lVar4 == 0) {
    FUN_ffff00009077de54
              (s__UFS___s__OTP_partition__s_is_no_ffff0000907e6d93,
               s_ufs_otp_lock_req_ffff0000907f72cd,param_1);
    uVar6 = 0xffffffed;
  }
  else {
    lVar5 = FUN_ffff00009077d294(0x40,0x1000);
    if (lVar5 == 0) {
      FUN_ffff00009077de54
                (s__UFS___s__alloc_buf_failed_ffff0000907f72de,s_ufs_otp_lock_req_ffff0000907f72cd);
      FUN_ffff00009077bce4(lVar4);
      uVar6 = 0xfffffff4;
    }
    else {
      uVar2 = FUN_ffff00009077bdf4(lVar4,lVar5,0,*(undefined8 *)(lVar4 + 0x28));
      if (((int)uVar2 < 0) || (*(ulong *)(lVar4 + 0x28) != (ulong)uVar2)) {
        FUN_ffff00009077de54
                  (s__UFS___s__read_failed_ffff0000907e04e2,s_ufs_otp_lock_req_ffff0000907f72cd);
        uVar6 = 0xffffffed;
        bVar1 = true;
      }
      else {
        iVar3 = FUN_ffff00009077f080(lVar5,&DAT_ffff0000907e3bce,4);
        bVar1 = false;
        uVar6 = (ulong)(iVar3 == 0);
      }
      thunk_FUN_ffff0000907c2ad8(lVar5);
      FUN_ffff00009077bce4(lVar4);
      if (!bVar1) {
        return uVar6;
      }
    }
  }
  FUN_ffff00009077de54
            (s__UFS___s__ret____d_ffff0000907d6eab,s_ufs_otp_lock_req_ffff0000907f72cd,uVar6);
  return uVar6;
}


