
undefined2 FUN_ffff0000b8013ba8(void)

{
  undefined2 uVar1;
  undefined1 uVar2;
  int iVar3;
  undefined8 uVar4;
  undefined1 local_24 [4];
  
  uVar4 = 0x2a;
  local_24[0] = 0x2a;
  iVar3 = FUN_ffff0000b8019bc8(10,0x55,local_24,1,1,0);
  uVar2 = local_24[0];
  if (iVar3 == 0) {
    uVar4 = 0x2b;
    local_24[0] = 0x2b;
    iVar3 = FUN_ffff0000b8019bc8(10,0x55,local_24,1,1,0);
    if (iVar3 == 0) {
      uVar1 = CONCAT11(local_24[0],uVar2);
      FUN_ffff0000b804c33c
                (s__XMCHG_LK_BQ28Z610___s__cycle_co_ffff0000b808a817,
                 s_bq28z610_get_cyclecount_ffff0000b808f9bc,uVar1,local_24[0],uVar2);
      return uVar1;
    }
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000b808f96a,
             s_bq28z610_read_byte_ffff0000b8086ff8,iVar3);
  FUN_ffff0000b804c33c
            (s__XMCHG_LK_BQ28Z610___s_I2C_faile_ffff0000b8093247,s_fg_read_block_ffff0000b80a0ef4,
             uVar4);
  return 0;
}


