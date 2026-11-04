
/* std::vector<std::string, std::allocator<std::string >
   >::vector[abi:ne180000](std::initializer_list<std::string >) */

void std::vector<std::string,std::allocator<std::string>>::vector_abi_ne180000_
               (undefined8 *param_1,string *param_2,ulong param_3)

{
  long lVar1;
  string *psVar2;
  undefined8 uVar3;
  undefined8 unaff_x22;
  undefined8 *local_58;
  undefined8 local_50;
  long local_48;

  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  *param_1 = 0;
  param_1[1] = 0;
  param_1[2] = 0;
  local_58 = param_1;
  if (param_3 != 0) {
    local_50 = 0;
    if (0xaaaaaaaaaaaaaaa < param_3) {
      if (*(long *)(lVar1 + 0x28) == local_48) {
        uVar3 = vector<unsigned_short,std::allocator<unsigned_short>>::
                __throw_length_error_abi_ne180000_();
        param_1[1] = unaff_x22;
        __destroy_vector::operator()[abi_nn180000_((__destroy_vector *)&local_58);
        if (*(long *)(lVar1 + 0x28) == local_48) {
                    /* WARNING: Subroutine does not return */
          _Unwind_Resume(uVar3);
        }
      }
      goto LAB_00228b70;
    }
    param_3 = param_3 * 0x18;
    psVar2 = operator_new(param_3);
    *param_1 = psVar2;
    param_1[1] = psVar2;
    param_1[2] = psVar2 + param_3;
    psVar2 = __uninitialized_allocator_copy_abi_ne180000_<std::allocator<std::string>,std::string_const*,std::string_const*,std::string*>
                       ((allocator *)(param_1 + 2),param_2,param_2 + param_3,psVar2);
    param_1[1] = psVar2;
  }
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
LAB_00228b70:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
