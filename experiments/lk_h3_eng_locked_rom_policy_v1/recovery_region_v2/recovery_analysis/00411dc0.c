
/* android::base::Split(std::string const&, std::string const&) */

void __thiscall android::base::Split(base *this,string *param_1,string *param_2)

{
  base *pbVar1;
  ulong uVar2;
  base *pbVar3;
  base *pbVar4;
  ulong uVar5;
  ulong *puVar6;
  ulong *puVar7;
  long lVar8;
  bool bVar9;
  void *__dest;
  string *psVar10;
  vector<std::string,std::allocator<std::string>> *in_x8;
  base *pbVar11;
  base *pbVar12;
  base *pbVar13;
  ulong uVar14;
  ulong uVar15;
  ulong local_80;
  ulong uStack_78;
  void *local_70;
  long local_68;

  lVar8 = tpidr_el0;
  local_68 = *(long *)(lVar8 + 0x28);
  uVar15 = (ulong)((byte)*param_1 >> 1);
  if (((byte)*param_1 & 1) != 0) {
    uVar15 = *(ulong *)(param_1 + 8);
  }
  if (uVar15 == 0) {
                    /* WARNING: Subroutine does not return */
    abort();
  }
  uVar15 = 0;
  *(undefined8 *)in_x8 = 0;
  *(undefined8 *)(in_x8 + 8) = 0;
  *(undefined8 *)(in_x8 + 0x10) = 0;
  do {
    uVar14 = 0xffffffffffffffff;
    bVar9 = ((byte)*this & 1) != 0;
    uVar2 = (ulong)((byte)*this >> 1);
    if (bVar9) {
      uVar2 = *(ulong *)(this + 8);
    }
    pbVar3 = this + 1;
    if (bVar9) {
      pbVar3 = *(base **)(this + 0x10);
    }
    pbVar4 = (base *)(param_1 + 1);
    uVar5 = (ulong)((byte)*param_1 >> 1);
    if (((byte)*param_1 & 1) != 0) {
      pbVar4 = *(base **)(param_1 + 0x10);
      uVar5 = *(ulong *)(param_1 + 8);
    }
    if ((uVar15 < uVar2) && (uVar5 != 0)) {
      pbVar1 = pbVar3 + uVar2;
      pbVar11 = pbVar3 + uVar15;
      do {
        uVar14 = uVar5;
        pbVar13 = pbVar4;
        do {
          pbVar12 = pbVar11;
          if (*pbVar11 == *pbVar13) goto LAB_00411ee0;
          pbVar13 = pbVar13 + 1;
          uVar14 = uVar14 - 1;
        } while (uVar14 != 0);
        pbVar11 = pbVar11 + 1;
        pbVar12 = pbVar1;
      } while (pbVar11 != pbVar1);
LAB_00411ee0:
      uVar14 = (long)pbVar12 - (long)pbVar3;
      if (pbVar12 == pbVar1) {
        uVar14 = 0xffffffffffffffff;
      }
    }
    if (uVar14 < uVar15) {
                    /* WARNING: Subroutine does not return */
      __ubsan_handle_sub_overflow_minimal_abort();
    }
    if (uVar2 < uVar15) {
      std::string::__throw_out_of_range_abi_nn180000_();
LAB_00411fd4:
                    /* WARNING: Subroutine does not return */
      std::string::__throw_length_error_abi_nn180000_();
    }
    uVar5 = uVar2 - uVar15;
    if (uVar14 - uVar15 <= uVar2 - uVar15) {
      uVar5 = uVar14 - uVar15;
    }
    if (0xffffffffffffffef < uVar5) goto LAB_00411fd4;
    if (uVar5 < 0x17) {
      local_80 = CONCAT71(local_80._1_7_,(char)((int)uVar5 << 1));
      __dest = (void *)((ulong)&local_80 | 1);
      if (uVar5 != 0) goto LAB_00411f50;
    }
    else {
      __dest = operator_new((uVar5 | 0xf) + 1);
      local_80 = (uVar5 | 0xf) + 2;
      uStack_78 = uVar5;
      local_70 = __dest;
LAB_00411f50:
      memmove(__dest,pbVar3 + uVar15,uVar5);
    }
    puVar6 = *(ulong **)(in_x8 + 8);
    puVar7 = *(ulong **)(in_x8 + 0x10);
    *(undefined1 *)((long)__dest + uVar5) = 0;
    if (puVar6 < puVar7) {
      puVar6[2] = (ulong)local_70;
      puVar6[1] = uStack_78;
      *puVar6 = local_80;
      *(ulong **)(in_x8 + 8) = puVar6 + 3;
    }
    else {
      psVar10 = std::vector<std::string,std::allocator<std::string>>::
                __push_back_slow_path<std::string>(in_x8,(string *)&local_80);
      *(string **)(in_x8 + 8) = psVar10;
      if ((local_80 & 1) != 0) {
        operator_delete(local_70);
      }
    }
    uVar15 = uVar14 + 1;
    if (0xfffffffffffffffe < uVar14) {
      if (*(long *)(lVar8 + 0x28) == local_68) {
        return;
      }
                    /* WARNING: Subroutine does not return */
      __stack_chk_fail();
    }
  } while( true );
}
