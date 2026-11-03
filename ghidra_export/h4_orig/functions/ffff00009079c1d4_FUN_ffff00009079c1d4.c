// ffff00009079c1d4  size=268  FUN_ffff00009079c1d4

undefined8 FUN_ffff00009079c1d4(undefined1 *param_1,undefined1 *param_2)

{
  uint uVar1;
  undefined1 uVar2;
  long lVar3;
  undefined4 uVar4;
  
  if ((param_1 != (undefined1 *)0x0) && (param_2 != (undefined1 *)0x0)) {
    if (param_1 != param_2) {
      uVar1 = *(uint *)(param_1 + 8);
      if (*(uint *)(param_2 + 4) < uVar1) {
        lVar3 = FUN_ffff00009077d5a8((ulong)uVar1,2);
        if (lVar3 == 0) {
          return 0xfffffffe;
        }
        FUN_ffff00009077eb20
                  (lVar3,*(undefined8 *)(param_1 + 0x10),(ulong)*(uint *)(param_1 + 8) << 1);
        if ((*(long *)(param_2 + 0x10) != 0) &&
           (FUN_ffff00009077ee50(*(long *)(param_2 + 0x10),0,(ulong)*(uint *)(param_2 + 4) << 1),
           *(long *)(param_2 + 0x10) != 0)) {
          thunk_FUN_ffff0000907c314c();
        }
        uVar4 = *(undefined4 *)(param_1 + 8);
        *(long *)(param_2 + 0x10) = lVar3;
        *(undefined4 *)(param_2 + 4) = uVar4;
      }
      else {
        FUN_ffff00009077ee50
                  (*(long *)(param_2 + 0x10) + (ulong)uVar1 * 2,0,
                   (ulong)(*(uint *)(param_2 + 4) - uVar1) << 1);
        FUN_ffff00009077eb20
                  (*(undefined8 *)(param_2 + 0x10),*(undefined8 *)(param_1 + 0x10),
                   (ulong)*(uint *)(param_1 + 8) << 1);
        uVar4 = *(undefined4 *)(param_1 + 8);
      }
      uVar2 = *param_1;
      *(undefined4 *)(param_2 + 8) = uVar4;
      *param_2 = uVar2;
    }
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_sboot_c_ffff0000907e064d,0x171,
             s_from______void__0_____to______vo_ffff0000907e88e5);
}


